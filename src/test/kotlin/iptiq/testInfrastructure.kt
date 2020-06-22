package iptiq

fun Provider.Companion.any() = Provider()

val ProviderId.Companion.id1 get() = ProviderId("ID1")
val ProviderId.Companion.id2 get() = ProviderId("ID2")

fun Provider.Companion.testInstance1() = Provider(ProviderId.id1)
fun Provider.Companion.testInstance2() = Provider(ProviderId.id2)
