// WITH_REFLECT
// FILE: J.java

@interface NoParams {}

@interface OneDefault {
    String foo() default "foo";
}

@interface OneNonDefault {
    String foo();
}

@interface TwoParamsOneDefault {
    String string();
    Class<?> clazz() default Object.class;
}

@interface TwoNonDefaultParams {
    String string();
    Class<?> clazz();
}

@interface ManyDefaultParameters {
    int i() default 0;
    String s() default "";
    double d() default 3.14;
}

// FILE: K.kt

import kotlin.reflect.KClass
import kotlin.reflect.primaryConstructor
import kotlin.test.assertEquals
import kotlin.test.assertFails

inline fun <T : Annotation> create(args: Map<String, Any?>): T {
    val ctor = T::class.constructors.single()
    return ctor.callBy(args.mapKeys { name -> ctor.parameters.single { it.name == name } })
}

fun box(): String {
    create<NoParams>()

    val t1 = create<OneDefault>()
    assertEquals("OK", t1.s)
    assertFails { create<OneDefault>(mapOf("s" to 42)) }

    val t2 = create<OneNonDefault>(mapOf("s" to "OK"))
    assertEquals("OK", t2.s)
    assertFails { create<OneNonDefault>() }

    val t3 = create<TwoParamsOneDefault>(mapOf("s" to "OK"))
    assertEquals("OK", t3.s)
    assertEquals(42, t3.x)
    val t4 = create<TwoParamsOneDefault>(mapOf("s" to "OK", "x" to 239))
    assertEquals(239, t4.x)
    assertFails { create<TwoParamsOneDefault>(mapOf("s" to "Fail", "x" to "Fail")) }

    assertFails("KClass (not Class) instances should be passed as arguments") {
        create<TwoNonDefaults>(mapOf("klass" to String::class.java, "string" to "Fail"))
    }

    val t5 = create<TwoNonDefaults>(mapOf("klass" to String::class, "string" to "OK"))
    return t5.string
}
