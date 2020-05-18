package thrift.server

import rpc.thrift.{DeviceOff, DeviceStatus, ResponseStatus, TemperatureUnit, Thermometer}

import scala.util.Random

class ThermometerHandler extends DeviceHandler with Thermometer.Iface {

  private val random = new Random()

  override def getTemperature(unit: TemperatureUnit): Double = {
    if(this.status == DeviceStatus.OFF) {
      throw new DeviceOff
    } else {
      if (unit == TemperatureUnit.C) {
        random.nextDouble() % 10 + 20
      } else {
        random.nextDouble() % 10 + 293
      }
    }
  }
}
