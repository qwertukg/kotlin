// WITH_REFLECT

import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor
import kotlin.test.assertEquals
import kotlin.test.assertFails

annotation class K(val string: String, val klass: KClass<*>)

fun box(): String {
    val kc = K::class.constructors.single()
    assertFails("call() should fail because arguments were passed in an incorrect order") {
        kc.call(Any::class, "Fail")
    }
    assertFails("call() should fail because KClass (not Class) instances should be passed as arguments") {
        kc.call("Fail", Any::class.java)
    }

    val k = kc.call("OK", Int::class)
    assertEquals(Int::class, k.klass as KClass<*>) // TODO: KT-9453

    return k.string
}
