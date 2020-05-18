namespace java rpc.thrift

enum ResponseStatus {
    OK,
    ERROR
}

enum DeviceStatus {
    ON,
    OFF
}

enum TemperatureUnit {
    C,
    K
}

service Device {
   ResponseStatus turnOn()
   ResponseStatus turnOff()
}

struct Vector {
    1: i32 x
    2: i32 y
}

exception DeviceOff {}

service IOTSensor extends Device {
    ResponseStatus rotate(1: Vector vector) throws (1: DeviceOff deviceOff)
}

service MovementSensor extends IOTSensor {
    bool detectMovement() throws (1: DeviceOff deviceOff)
}

service CCTV extends IOTSensor {
    list<list<double>> getReading() throws (1: DeviceOff deviceOff)
}

service Thermometer extends Device {
    double getTemperature(1: TemperatureUnit unit) throws (1: DeviceOff deviceOff)
}
