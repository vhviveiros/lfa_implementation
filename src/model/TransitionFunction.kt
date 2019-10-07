package model

class TransitionFunction(val fromState: State, val alphabet: Alphabet, val toState: State) {
    override fun equals(other: Any?) = other is TransitionFunction
            && other.fromState == fromState
            && other.toState == toState
            && other.alphabet == alphabet

    override fun toString() = "$fromState--($alphabet)-->$toState"
}