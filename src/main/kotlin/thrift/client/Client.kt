package thrift.client

import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.protocol.TMultiplexedProtocol
import org.apache.thrift.transport.TSocket
import rpc.thrift.*
import java.lang.Exception

fun main() {

    try {
        val transport = TSocket("localhost", 9080)
        val protocol = TBinaryProtocol(transport, true, true)
        val thermometers: List<Thermometer.Client> = (1..3).map{
            Thermometer.Client(TMultiplexedProtocol(protocol, "Thermometer-$it"))
        }
        val cctvs: List<CCTV.Client> = (1..2).map{
            CCTV.Client(TMultiplexedProtocol(protocol, "CCTV-$it"))
        }
        val movementSensors: List<MovementSensor.Client> = (1..2).map{
            MovementSensor.Client(TMultiplexedProtocol(protocol, "MovementSensor-$it"))
        }

        transport.open()

        loop@ while (true) {
            try {
                System.out.print("client> ")
                System.out.flush()
                val input = readLine()!!
                val tokens = input.split(' ')
                when(tokens[0]){
                    "THERMOMETER" -> handleThermometer(thermometers, tokens.toTypedArray())
                    "CCTV" -> handleCCTV(cctvs, tokens.toTypedArray())
                    "MOVEMENT_SENSOR" -> handleMovementSensor(movementSensors, tokens.toTypedArray())
                    "EXIT" -> {
                        transport.close()
                        break@loop
                    }
                    else -> handleWrongInput()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun handleCCTV(cctvs: List<CCTV.Client>, tokens: Array<String>) {
    if (tokens.size >= 3 && tokens[1].toInt() >= 0 && tokens[1].toInt() < cctvs.size) {
        when(tokens[2]) {
            "READ" -> {
                try {
                    val reading = cctvs[tokens[1].toInt() - 1].getReading()
                    handleResponse(reading.toString())
                } catch (e: DeviceOff) {
                    println("Device is OFF. Turn it ON before using it!")
                }
            }
            "ROTATE" -> {
                try {
                    val v = Vector(tokens[3].toInt(), tokens[4].toInt())
                    val reading = cctvs[tokens[1].toInt() - 1].rotate(v)
                    handleResponse(reading.toString())
                } catch (e: DeviceOff) {
                    println("Device is OFF. Turn it ON before using it!")
                }
            }
            "TURN_ON" -> handleResponse(cctvs[tokens[1].toInt() - 1].turnOn().toString())
            "TURN_OFF" -> handleResponse(cctvs[tokens[1].toInt() - 1].turnOff().toString())
            else -> handleWrongInput()
        }
    } else {
        handleWrongInput()
    }
}

fun handleMovementSensor(movementSensors: List<MovementSensor.Client>, tokens: Array<String>) {
    if (tokens.size >= 3 && tokens[1].toInt() >= 0 && tokens[1].toInt() < movementSensors.size) {
        when(tokens[2]) {
            "DETECT" -> {
                try {
                    val reading = movementSensors[tokens[1].toInt() - 1].detectMovement()
                    handleResponse(reading.toString())
                } catch (e: DeviceOff) {
                    println("Device is OFF. Turn it ON before using it!")
                }
            }
            "ROTATE" -> {
                try {
                    val v = Vector(tokens[3].toInt(), tokens[4].toInt())
                    val reading = movementSensors[tokens[1].toInt() - 1].rotate(v)
                    handleResponse(reading.toString())
                } catch (e: DeviceOff) {
                    println("Device is OFF. Turn it ON before using it!")
                }
            }
            "TURN_ON" -> handleResponse(movementSensors[tokens[1].toInt() - 1].turnOn().toString())
            "TURN_OFF" -> handleResponse(movementSensors[tokens[1].toInt() - 1].turnOff().toString())
            else -> handleWrongInput()
        }
    } else {
        handleWrongInput()
    }
}

fun handleThermometer(thermometers: List<Thermometer.Client>, tokens: Array<String>) {
    if (tokens.size >= 3 && tokens[1].toInt() >= 0 && tokens[1].toInt() < thermometers.size) {
        when(tokens[2]) {
            "GET_TEMP" -> handleGetTemp(thermometers, tokens)
            "TURN_ON" -> handleResponse(thermometers[tokens[1].toInt() - 1].turnOn().toString())
            "TURN_OFF" -> handleResponse(thermometers[tokens[1].toInt() - 1].turnOff().toString())
            else -> handleWrongInput()
        }
    } else {
        handleWrongInput()
    }
}

fun handleGetTemp(thermometers: List<Thermometer.Client>, tokens: Array<String>) {
    if(tokens.size >= 4 && tokens[3] == "K") {
        try {
            val reading = thermometers[tokens[1].toInt() - 1].getTemperature(TemperatureUnit.K)
            handleResponse(reading.toString())
        } catch (e: DeviceOff) {
            println("Device is OFF. Turn it ON before using it!")
        }
    } else {
        try {
            val reading = thermometers[tokens[1].toInt() - 1].getTemperature(TemperatureUnit.C)
            handleResponse(reading.toString())
        } catch (e: DeviceOff) {
            println("Device is OFF. Turn it ON before using it!")
        }
    }
}

fun handleWrongInput() {
    println("Wrong input!")
    println("Input format: SENSOR_TYPE SENSOR_NUMBER ACTION_NAME [ACTION_PARAMS]")
}

fun handleResponse(str: String) {
    println("[RESPONSE] $str")
}