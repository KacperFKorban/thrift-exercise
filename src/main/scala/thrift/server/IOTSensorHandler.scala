package thrift.server

import rpc.thrift
import rpc.thrift.{DeviceOff, DeviceStatus, IOTSensor, ResponseStatus}

trait IOTSensorHandler extends DeviceHandler with IOTSensor.Iface {

  private var x: Int = 0
  private var y: Int = 0

  override def rotate(vector: thrift.Vector): ResponseStatus = {
    if(this.status == DeviceStatus.OFF) {
      throw new DeviceOff
    } else {
      val newX = x + vector.getX
      val newY = y + vector.getY
      if (newX < -90 || newX > 90 || newY < -90 || newY > 90) {
        ResponseStatus.ERROR
      } else {
        this.x = newX
        this.y = newY
        ResponseStatus.OK
      }
    }
  }
}
