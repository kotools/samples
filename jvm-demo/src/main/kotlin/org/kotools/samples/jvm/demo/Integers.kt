package org.kotools.samples.jvm.demo

/**
 * Returns this integer if greater than zero, or returns `null` otherwise.
 *
 * <br>
 * <details>
 * <summary>
 *     <b>Calling from Kotlin</b>
 * </summary>
 *
 * Here's an example of calling this function from Kotlin code:
 *
 * SAMPLE: [org.kotools.samples.jvm.demo.IntegersKotlinSample.takeIfPositive]
 * </details>
 * <br>
 *
 * This function is unavailable from Java code due to its non-explicit support
 * for nullable values.
 */
@JvmSynthetic
public fun Int.takeIfPositive(): Int? = this.takeIf { it > 0 }
