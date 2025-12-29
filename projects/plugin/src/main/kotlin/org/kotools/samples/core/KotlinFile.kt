package org.kotools.samples.core

import kotlinx.ast.common.AstSource
import kotlinx.ast.common.ast.Ast
import kotlinx.ast.common.ast.AstAttachmentRawAst
import kotlinx.ast.common.ast.AstNode
import kotlinx.ast.common.ast.AstTerminal
import kotlinx.ast.common.flattenTerminal
import kotlinx.ast.common.klass.KlassDeclaration
import kotlinx.ast.common.klass.KlassIdentifier
import kotlinx.ast.common.klass.RawAst
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.common.summary.PackageHeader
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser
import java.io.File

@JvmInline
internal value class KotlinFile private constructor(private val file: File) {
    // ------------------------------- Creations -------------------------------

    companion object {
        fun fromOrNull(file: File): KotlinFile? =
            if (file.extension != "kt") null
            else KotlinFile(file)
    }

    // ---------------------------- File operations ----------------------------

    fun samples(): Set<KotlinSample> {
        val nodes: List<Ast> = this.parseNodes()
        this.checkAbsenceOfTopLevelFunctions(nodes)
        val packageIdentifier: String? = this.packageIdentifierOrNull(nodes)
        return nodes.filterIsInstance<KlassDeclaration>()
            .flatMap { this.classSamples(declaration = it, packageIdentifier) }
            .toSet()
    }

    private fun parseNodes(): List<Ast> {
        val source: AstSource.File = AstSource.File(this.file.path)
        return KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
            .summary(attachRawAst = false)
            .get()
    }

    private fun checkAbsenceOfTopLevelFunctions(nodes: List<Ast>) {
        val topLevelFunctionFound: Boolean = nodes
            .filterIsInstance<KlassDeclaration>()
            .any { it.keyword == "fun" }
        if (topLevelFunctionFound) throw FileSystemException(
            this.file,
            reason = "Top-level function found in Kotlin sample source."
        )
    }

    private fun packageIdentifierOrNull(nodes: List<Ast>): String? = nodes
        .filterIsInstance<PackageHeader>()
        .flatMap(PackageHeader::identifier)
        .joinToString(
            separator = ".",
            transform = KlassIdentifier::identifier
        )
        .ifBlank { null }

    private fun classSamples(
        declaration: KlassDeclaration,
        packageIdentifier: String?
    ): Set<KotlinSample> {
        if (declaration.keyword != "class") return emptySet()
        val className: String = packageIdentifier
            ?.let { "${it}.${declaration.identifier?.identifier}" }
            ?: "${declaration.identifier?.identifier}"
        return declaration.expressions.asSequence()
            .filter { it.description == "classBody" }
            .filterIsInstance<AstNode>()
            .flatMap(AstNode::children)
            .filterIsInstance<KlassDeclaration>()
            .mapNotNull { this.sampleOrNull(declaration = it, className) }
            .toSet()
    }

    private fun sampleOrNull(
        declaration: KlassDeclaration,
        className: String
    ): KotlinSample? {
        if (declaration.keyword != "fun") return null
        val identifier = "${className}.${declaration.identifier?.identifier}"
        val raw: RawAst = declaration.attachments
            .get<RawAst>(AstAttachmentRawAst)
            ?: error("No raw AST available (was: ${declaration.description}).")
        val text: String = raw.ast.flattenTerminal()
            .joinToString(separator = "", transform = AstTerminal::text)
        val singleExpressionRegex =
            Regex("""fun [A-Za-z_]+\(\)(?:: [A-Za-z]+)? = .+$""")
        val content: String =
            if (singleExpressionRegex in text) text.substringAfter(" = ")
                .trimIndent()
            else text.substringAfter('{')
                .substringBefore('}')
                .trimIndent()
        return KotlinSample.from(identifier, content)
    }

    // ------------------------------ Conversions ------------------------------

    override fun toString(): String = "'${this.file}' Kotlin sample source"
}
