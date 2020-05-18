package thrift.server

import rpc.thrift.{DeviceOff, DeviceStatus, MovementSensor}

import scala.util.Random

class MovementSensorHandler extends DeviceHandler with IOTSensorHandler with MovementSensor.Iface {

  private val random = new Random()

  override def detectMovement(): Boolean = {
    if(this.status == DeviceStatus.OFF) {
      throw new DeviceOff
    } else {
      random.nextBoolean()
    }
  }
}
