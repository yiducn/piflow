package cn.piflow.bundle.file

import java.net.URI

import cn.piflow.{JobContext, JobInputStream, JobOutputStream, ProcessContext}
import cn.piflow.conf.{ConfigurableStop, FileGroup, StopGroup}
import cn.piflow.conf.bean.PropertyDescriptor
import cn.piflow.conf.util.MapUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

class FetchFile extends ConfigurableStop{
  override val authorEmail: String = "xiaoxiao@cnic.cn"
  val inportCount: Int = 0
  val outportCount: Int = 1
  var hdfs_path:String =_
  var local_path:String=_
  var fs:FileSystem=null

  def perform(in: JobInputStream, out: JobOutputStream, pec: JobContext): Unit = {
    initFs(hdfs_path)
    downloadFiletoLocal(hdfs_path,local_path)
  }

  def initFs(uri:String):Unit={
    val conf:Configuration=new Configuration()
    fs= FileSystem.get(new URI(uri),conf)
  }

  def downloadFiletoLocal(hdfs_path:String,local_path:String):Unit={
    val src:Path=new Path(hdfs_path)
    val dst:Path=new Path(local_path)
    fs.copyToLocalFile(src,dst)
  }

  def initialize(ctx: ProcessContext): Unit = {

  }


  def setProperties(map: Map[String, Any]): Unit = {
    hdfs_path=MapUtil.get(map,key="hdfs_path").asInstanceOf[String]
    local_path=MapUtil.get(map,key="local_path").asInstanceOf[String]
  }

  override def getPropertyDescriptor(): List[PropertyDescriptor] = {
    var descriptor : List[PropertyDescriptor] = List()
    val hdfs_path = new PropertyDescriptor().name("hdfs_path").displayName("HDFS_PATH").defaultValue("").required(true)
    val local_path = new PropertyDescriptor().name("local_path").displayName("LOCAL_PATH").defaultValue("").required(true)
    descriptor = hdfs_path :: descriptor
    descriptor = local_path :: descriptor
    descriptor
  }

  override def getIcon(): Array[Byte] = ???

  override def getGroup(): StopGroup = {
    FileGroup
  }



}
