package org.kotools.samples.conventions

import org.gradle.api.provider.Property

/** Extension for [CompatibilityPlugin]. */
public interface CompatibilityExtension {
    /** Java version to be compatible with. */
    public val java: Property<String>

    /** Kotlin version to be compatible with. */
    public val kotlin: Property<String>
}
