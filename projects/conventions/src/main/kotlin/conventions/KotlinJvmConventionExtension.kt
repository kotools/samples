package conventions

import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

/** Extension for configuring the `conventions.kotlin.jvm` plugin. */
public interface KotlinJvmConventionExtension {
    /**
     * The version of Kotlin core libraries to use.
     *
     * See [KotlinTopLevelExtension.coreLibrariesVersion] property for more
     * details.
     */
    public val coreLibrariesVersion: Property<String>
}
