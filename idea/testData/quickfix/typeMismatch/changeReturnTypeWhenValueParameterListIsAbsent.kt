// "Change 'A.hasNext' function return type to 'Boolean'" "true"
abstract class A {
    abstract operator fun hasNext
    abstract operator fun next(): Int
    abstract operator fun iterator(): A
}

fun test(notRange: A) {
    for (i in notRange<caret>) {}
}