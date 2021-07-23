package com.example.pkl_v1.service

class SensorService {
}
fun Accelero(){

}
fun wait(ms: Int) {
    try {
        Thread.sleep(ms.toLong())
    } catch (ex: InterruptedException) {
        Thread.currentThread().interrupt()
    }
}