import model.*

fun main() {
    val states = arrayOf(State(true), State(), State(), State())
    val alphabet = arrayOf(Alphabet('a'), Alphabet('b'), Alphabet('c'))

    val a = TransitionFunctionCollection()
    a.insert(TransitionFunction(states[0], states[1], alphabet[0]))
    a.insert(TransitionFunction(states[1], states[1], alphabet[1]))
    a.insert(TransitionFunction(states[0], states[2], alphabet[2]))
    a.insert(TransitionFunction(states[2], states[3], alphabet[2]))
    a.insert(TransitionFunction(states[3], states[1], alphabet[0]))

    val m = TransitionMatrix(a)

    print(m)
}