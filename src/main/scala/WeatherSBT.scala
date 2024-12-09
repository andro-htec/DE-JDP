import org.jsoup.Jsoup
import java.io.{BufferedWriter, File, FileWriter}
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object WeatherApp extends App {

  val url = "https://api.openweathermap.org/data/2.5/weather?lat=45.23&lon=19.82&appid=85f41b4ae0dd98de437bed7a11bf8bad&units=metric"
  val response = Jsoup.connect(url).ignoreContentType(true).execute().body()

  val weatherJson = ujson.read(response)
  val city = weatherJson("name").str
  val lat = weatherJson("coord")("lat").num
  val lon = weatherJson("coord")("lon").num
  val temperature = weatherJson("main")("temp").num
  val feelsLike = weatherJson("main")("feels_like").num
  val tempMin = weatherJson("main")("temp_min").num
  val tempMax = weatherJson("main")("temp_max").num
  val pressure = weatherJson("main")("pressure").num.toInt
  val humidity = weatherJson("main")("humidity").num.toInt
  val seaLevel = weatherJson("main").obj.get("sea_level").map(_.num.toInt).getOrElse(-1)
  val sunrise = weatherJson("sys")("sunrise").num.toLong
  val sunset = weatherJson("sys")("sunset").num.toLong

  val currentDate = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
  val sunriseTime = formatter.format(Instant.ofEpochSecond(sunrise))
  val sunsetTime = formatter.format(Instant.ofEpochSecond(sunset))

  val filePath = "weather_data.csv"

  val header =
    "City,Lat,Long,Date,Temperature,FeelsLike,TempMin,TempMax,Pressure,Humidity,SeaLevel,Sunrise,Sunset\n"
  val row =
    s"$city,$lat,$lon,$currentDate,$temperature,$feelsLike,$tempMin,$tempMax,$pressure,$humidity,$seaLevel,$sunriseTime,$sunsetTime\n"

  val file = new File(filePath)
  val writer = new BufferedWriter(new FileWriter(file, false))
  if (!file.exists() || file.length() == 0) {
    writer.write(header)
  }
  writer.write(row)
  writer.close()

  println(s"Weather data written to $filePath")
}
