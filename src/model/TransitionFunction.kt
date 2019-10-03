package model

class TransitionFunction(val fromState: State, val alphabet: Alphabet, val toState: State) {
    override fun toString() = "$fromState--($alphabet)-->$toState"
}