package model

typealias TransitionFunctions = ArrayList<TransitionFunction>

class TransitionFunctionCollection : Iterable<TransitionFunction> {
    val functions = TransitionFunctions()
    val stateCollection = ArrayList<State>()
    val alphabetCollection = ArrayList<Alphabet>()
    val size
        get() = functions.size

    fun next(state: State, alphabet: Alphabet) = functions.filter {
        it.fromState == state && it.alphabet == alphabet
    }.map { it.toState }

    fun insert(transitionFunction: TransitionFunction) {
        functions.add(transitionFunction)

        if (!stateCollection.contains(transitionFunction.fromState))
            stateCollection.add(transitionFunction.fromState)

        if (!stateCollection.contains(transitionFunction.toState))
            stateCollection.add(transitionFunction.toState)

        if (!alphabetCollection.contains(transitionFunction.alphabet))
            alphabetCollection.add(transitionFunction.alphabet)
    }

    fun remove(transitionFunction: TransitionFunction) {
        functions.remove(transitionFunction)

        val alphabet = transitionFunction.alphabet
        val states = arrayOf(transitionFunction.fromState, transitionFunction.toState)

        if (functions.none { it.alphabet == alphabet })
            alphabetCollection.remove(alphabet)

        if (functions.none { states.contains(it.fromState) })
            stateCollection.remove(states[0])

        if (functions.none { states.contains(it.toState) })
            stateCollection.remove(states[1])
    }

    fun toMatrix() = TransitionMatrix(this)

    override fun iterator(): Iterator<TransitionFunction> = functions.iterator()

    override fun toString() = functions.toString().replace(',', '\n')
}