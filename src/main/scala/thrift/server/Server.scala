package thrift.server

import org.apache.thrift.TMultiplexedProcessor
import org.apache.thrift.protocol.{TBinaryProtocol, TProtocolFactory}
import org.apache.thrift.server.TServer.Args
import org.apache.thrift.server.TSimpleServer
import org.apache.thrift.transport.{TServerSocket, TServerTransport}
import rpc.thrift.{CCTV, MovementSensor, Thermometer}

object Server extends App {
  try {
    println("Server started")
    val serverTransport: TServerTransport = new TServerSocket(9080)
    val protocolFactory: TProtocolFactory = new TBinaryProtocol.Factory()
    val multiplex = new TMultiplexedProcessor()
    (1 to 3).foreach { i =>
      multiplex.registerProcessor(s"Thermometer-$i", new Thermometer.Processor(new ThermometerHandler()))
    }
    (1 to 2).foreach { i =>
      multiplex.registerProcessor(s"MovementSensor-$i", new MovementSensor.Processor(new MovementSensorHandler()))
    }
    (1 to 2).foreach { i =>
      multiplex.registerProcessor(s"CCTV-$i", new CCTV.Processor(new CCTVHandler()))
    }
    new TSimpleServer(new Args(serverTransport).protocolFactory(protocolFactory).processor(multiplex)).serve()
  } catch {
    case e: Exception => e.printStackTrace()
  }
}
