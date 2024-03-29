package model

class Alphabet(val char: Char) {
    override fun equals(other: Any?) = other is Alphabet && other.char == char

    override fun toString() = "$char"

    override fun hashCode() = char.hashCode()

    fun isVoid() = char == '&'
}