class NoPackageValidSample {
    fun greet() {
        val name = "Sample"
        val greeting: String = greet(name)
        val expected = "Hello $name!"
        assertEquals(expected, greeting)
    }
}
