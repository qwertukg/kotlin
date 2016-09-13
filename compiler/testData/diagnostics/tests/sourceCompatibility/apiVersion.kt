// !DIAGNOSTICS: -INVISIBLE_REFERENCE -INVISIBLE_MEMBER
// !API_VERSION: 1.0

import kotlin.internal.Since

@Since("1.0")
fun ok() {}

@Since("1.1")
fun f() {}

@Since("1.1")
val p = Unit

@Since("1.1")
open class Foo

class Bar @Since("1.1") constructor()

@Since("1.1")
annotation class Anno1(val s: String)

annotation class Anno2 @Since("1.1") constructor()

// ------------------------

fun t0() = ok()

fun t1() = <!API_NOT_AVAILABLE!>f<!>()
fun t2() = <!API_NOT_AVAILABLE!>p<!>
fun t3(): <!API_NOT_AVAILABLE!>Foo<!> = <!API_NOT_AVAILABLE!>Foo<!>()
fun t4() = object : <!API_NOT_AVAILABLE!>Foo<!>() {}
fun t5(): Bar? = <!API_NOT_AVAILABLE!>Bar<!>()

@<!API_NOT_AVAILABLE!>Anno1<!>("")
@<!API_NOT_AVAILABLE!>Anno2<!>
fun t6() = Unit
