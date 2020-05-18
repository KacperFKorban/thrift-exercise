package thrift.server

import java.{lang, util}

import rpc.thrift.{CCTV, DeviceOff, DeviceStatus, ResponseStatus}

import scala.util.Random
import scala.collection.JavaConversions._

class CCTVHandler extends DeviceHandler with IOTSensorHandler with CCTV.Iface {

  private val random = new Random()

  override def getReading: util.List[util.List[lang.Double]] = {
    if(this.status == DeviceStatus.OFF) {
      throw new DeviceOff
    } else {
      (1 to 10)
        .map { _ =>
          (1 to 10)
            .map { _ =>
              lang.Double.valueOf(random.nextDouble() % 2)
            }: util.List[lang.Double]
        }
    }
  }
}
