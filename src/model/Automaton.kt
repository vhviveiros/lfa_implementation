package model

class Automaton(val states: States, val alphabet: Alphabet, val transitionFunctionCollection: TransitionFunctionCollection) {
    fun getInitialState() = states.first { it.isInitial }

    fun getFinalStates() = states.filter { it.isFinal }

    fun buildMatrix() {

    }
}