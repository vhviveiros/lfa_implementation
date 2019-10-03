package model

class TransitionFunction(val fromState: State, val toState: State, val alphabet: Alphabet) {
    override fun toString() = "$fromState--($alphabet)-->$toState"
}