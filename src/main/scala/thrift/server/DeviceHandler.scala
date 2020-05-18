package thrift.server

import rpc.thrift.{Device, DeviceStatus, ResponseStatus}

trait DeviceHandler extends Device.Iface {

  var status: DeviceStatus = DeviceStatus.OFF

  override def turnOn(): ResponseStatus = {
    if(this.status == DeviceStatus.ON) {
      ResponseStatus.ERROR
    } else {
      this.status = DeviceStatus.ON
      ResponseStatus.OK
    }
  }

  override def turnOff(): ResponseStatus = {
    if(this.status == DeviceStatus.OFF) {
      ResponseStatus.ERROR
    } else {
      this.status = DeviceStatus.OFF
      ResponseStatus.OK
    }
  }
}
