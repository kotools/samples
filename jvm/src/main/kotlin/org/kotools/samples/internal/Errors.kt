package org.kotools.samples.internal

internal fun String.sampleNotFound(): String {
    require(this.isSampleIdentifier()) {
        "String is not a sample identifier (input: '$this')."
    }
    return "'$this' sample not found."
}
