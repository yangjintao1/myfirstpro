/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flink.table.util

import java.sql.{Date => SqlDate, Time => SqlTime, Timestamp => SqlTimestamp}

import org.apache.flink.api.scala._
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.expressions.{Expression, ExpressionParser}
import org.apache.flink.util.TimeConvertUtils
import org.apache.flink.util.TimeConvertUtils.MILLIS_PER_DAY
import org.apache.hadoop.io.IntWritable

import scala.collection.mutable
import scala.util.Random

/**
 * #################################################################################################
 *
 * BE AWARE THAT OTHER TESTS DEPEND ON THIS TEST DATA.
 * IF YOU MODIFY THE DATA MAKE SURE YOU CHECK THAT ALL TESTS ARE STILL WORKING!
 *
 * #################################################################################################
 */
object CollectionBatchExecTable {
  def get3TupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, Long, String)]
    data.+=((1, 1L, "Hi"))
    data.+=((2, 2L, "Hello"))
    data.+=((3, 2L, "Hello world"))
    data.+=((4, 3L, "Hello world, how are you?"))
    data.+=((5, 3L, "I am fine."))
    data.+=((6, 3L, "Luke Skywalker"))
    data.+=((7, 4L, "Comment#1"))
    data.+=((8, 4L, "Comment#2"))
    data.+=((9, 4L, "Comment#3"))
    data.+=((10, 4L, "Comment#4"))
    data.+=((11, 5L, "Comment#5"))
    data.+=((12, 5L, "Comment#6"))
    data.+=((13, 5L, "Comment#7"))
    data.+=((14, 5L, "Comment#8"))
    data.+=((15, 5L, "Comment#9"))
    data.+=((16, 6L, "Comment#10"))
    data.+=((17, 6L, "Comment#11"))
    data.+=((18, 6L, "Comment#12"))
    data.+=((19, 6L, "Comment#13"))
    data.+=((20, 6L, "Comment#14"))
    data.+=((21, 6L, "Comment#15"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmall3TupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, Long, String)]
    data.+=((1, 1L, "Hi"))
    data.+=((2, 2L, "Hello"))
    data.+=((3, 2L, "Hello world"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def get5TupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, Long, Int, String, Long)]
    data.+=((1, 1L, 0, "Hallo", 1L))
    data.+=((2, 2L, 1, "Hallo Welt", 2L))
    data.+=((2, 3L, 2, "Hallo Welt wie", 1L))
    data.+=((3, 4L, 3, "Hallo Welt wie gehts?", 2L))
    data.+=((3, 5L, 4, "ABC", 2L))
    data.+=((3, 6L, 5, "BCD", 3L))
    data.+=((4, 7L, 6, "CDE", 2L))
    data.+=((4, 8L, 7, "DEF", 1L))
    data.+=((4, 9L, 8, "EFG", 1L))
    data.+=((4, 10L, 9, "FGH", 2L))
    data.+=((5, 11L, 10, "GHI", 1L))
    data.+=((5, 12L, 11, "HIJ", 3L))
    data.+=((5, 13L, 12, "IJK", 3L))
    data.+=((5, 14L, 13, "JKL", 2L))
    data.+=((5, 15L, 14, "KLM", 2L))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmall5TupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, Long, Int, String, Long)]
    data.+=((1, 1L, 0, "Hallo", 1L))
    data.+=((2, 2L, 1, "Hallo Welt", 2L))
    data.+=((2, 3L, 2, "Hallo Welt wie", 1L))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def get7TupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, Long, Double, String, SqlDate, SqlTime, SqlTimestamp)]
    data.+=((1, 1L, 99.99, "Hi",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-10") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:23:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 02:00:00"))))
    data.+=((2, 2L, 89.89, "Hello",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-11") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:25:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 03:00:00"))))
    data.+=((3, 2L, 79.79, null,
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-12") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:27:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 04:00:00"))))
    data.+=((4, 3L, 69.69, null,
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-13") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:29:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 05:00:00"))))
    data.+=((5, 3L, 59.59, "I am fine.",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-14") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:31:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 06:00:00"))))
    data.+=((6, 3L, 49.49, "Luke Skywalker",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-15") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:33:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 07:00:00"))))
    data.+=((7, 4L, 59.59, "Comment#1",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-10") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:35:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 08:00:00"))))
    data.+=((8, 4L, 69.69, "Comment#2",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-11") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:23:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 09:00:00"))))
    data.+=((9, 4L, 79.79, "Comment#3",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-12") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:23:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 06:00:00"))))
    data.+=((10, 4L, 89.89, "Comment#4",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-13") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:25:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 06:00:00"))))
    data.+=((11, 5L, 99.99, "Comment#5",
      new SqlDate(TimeConvertUtils.dateStringToUnixDate("2017-10-14") * MILLIS_PER_DAY),
      new SqlTime(TimeConvertUtils.timeStringToUnixDate("22:25:24")),
      new SqlTimestamp(TimeConvertUtils.timestampStringToUnixDate("2017-10-12 06:00:00"))))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmallNestedTupleDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[((Int, Int), String)]
    data.+=(((1, 1), "one"))
    data.+=(((2, 2), "two"))
    data.+=(((3, 3), "three"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getGroupSortedNestedTupleDataSet(env: BatchTableEnvironment, fields: String = null)
    : Table = {
    val data = new mutable.MutableList[((Int, Int), String)]
    data.+=(((1, 3), "a"))
    data.+=(((1, 2), "a"))
    data.+=(((2, 1), "a"))
    data.+=(((2, 2), "b"))
    data.+=(((3, 3), "c"))
    data.+=(((3, 6), "c"))
    data.+=(((4, 9), "c"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getStringDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[String]
    data.+=("Hi")
    data.+=("Hello")
    data.+=("Hello world")
    data.+=("Hello world, how are you?")
    data.+=("I am fine.")
    data.+=("Luke Skywalker")
    data.+=("Random comment")
    data.+=("LOL")
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getIntDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[Int]
    data.+=(1)
    data.+=(2)
    data.+=(2)
    data.+=(3)
    data.+=(3)
    data.+=(3)
    data.+=(4)
    data.+=(4)
    data.+=(4)
    data.+=(4)
    data.+=(5)
    data.+=(5)
    data.+=(5)
    data.+=(5)
    data.+=(5)
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getCustomTypeDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[CustomType]
    data.+=(new CustomType(1, 0L, "Hi"))
    data.+=(new CustomType(2, 1L, "Hello"))
    data.+=(new CustomType(2, 2L, "Hello world"))
    data.+=(new CustomType(3, 3L, "Hello world, how are you?"))
    data.+=(new CustomType(3, 4L, "I am fine."))
    data.+=(new CustomType(3, 5L, "Luke Skywalker"))
    data.+=(new CustomType(4, 6L, "Comment#1"))
    data.+=(new CustomType(4, 7L, "Comment#2"))
    data.+=(new CustomType(4, 8L, "Comment#3"))
    data.+=(new CustomType(4, 9L, "Comment#4"))
    data.+=(new CustomType(5, 10L, "Comment#5"))
    data.+=(new CustomType(5, 11L, "Comment#6"))
    data.+=(new CustomType(5, 12L, "Comment#7"))
    data.+=(new CustomType(5, 13L, "Comment#8"))
    data.+=(new CustomType(5, 14L, "Comment#9"))
    data.+=(new CustomType(6, 15L, "Comment#10"))
    data.+=(new CustomType(6, 16L, "Comment#11"))
    data.+=(new CustomType(6, 17L, "Comment#12"))
    data.+=(new CustomType(6, 18L, "Comment#13"))
    data.+=(new CustomType(6, 19L, "Comment#14"))
    data.+=(new CustomType(6, 20L, "Comment#15"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmallCustomTypeDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[CustomType]
    data.+=(new CustomType(1, 0L, "Hi"))
    data.+=(new CustomType(2, 1L, "Hello"))
    data.+=(new CustomType(2, 2L, "Hello world"))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmallTuplebasedPojoMatchingDataSet(env: BatchTableEnvironment, fields: String = null):
    Table = {
    val data = new mutable.MutableList[(Int, String, Int, Int, Long, String, Long)]
    data.+=((1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=((2, "Second", 20, 200, 2000L, "Two", 20000L))
    data.+=((3, "Third", 30, 300, 3000L, "Three", 30000L))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getSmallPojoDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[POJO]
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(2, "Second", 20, 200, 2000L, "Two", 20000L))
    data.+=(new POJO(3, "Third", 30, 300, 3000L, "Three", 30000L))
    env.fromCollection(Random.shuffle(data), fields)
  }

  def getDuplicatePojoDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[POJO]
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10000L))
    data.+=(new POJO(2, "Second", 20, 200, 2000L, "Two", 20000L))
    data.+=(new POJO(3, "Third", 30, 300, 3000L, "Three", 30000L))
    data.+=(new POJO(3, "Third", 30, 300, 3000L, "Three", 30000L))
    env.fromCollection(data, fields)
  }

  def getCrazyNestedDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[CrazyNested]
    data.+=(new CrazyNested("aa"))
    data.+=(new CrazyNested("bb"))
    data.+=(new CrazyNested("bb"))
    data.+=(new CrazyNested("cc"))
    data.+=(new CrazyNested("cc"))
    data.+=(new CrazyNested("cc"))
    env.fromCollection(data, fields)
  }

  def getPojoContainingTupleAndWritable(env: BatchTableEnvironment, fields: String = null)
    : Table = {
    val data = new
        mutable.MutableList[PojoContainingTupleAndWritable]
    data.+=(new PojoContainingTupleAndWritable(1, 10L, 100L))
    data.+=(new PojoContainingTupleAndWritable(2, 20L, 200L))
    data.+=(new PojoContainingTupleAndWritable(2, 20L, 200L))
    data.+=(new PojoContainingTupleAndWritable(2, 20L, 200L))
    data.+=(new PojoContainingTupleAndWritable(2, 20L, 200L))
    data.+=(new PojoContainingTupleAndWritable(2, 20L, 200L))
    env.fromCollection(data, fields)
  }

  def getGroupSortedPojoContainingTupleAndWritable(
      env: BatchTableEnvironment,
      fields: String = null): Table = {
    val data = new mutable.MutableList[PojoContainingTupleAndWritable]
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(1, 10L, 100L))
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(2, 20L, 200L))
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(2, 20L, 201L))
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(2, 30L, 200L))
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(2, 30L, 600L))
    data.+=(new CollectionBatchExecTable.PojoContainingTupleAndWritable(2, 30L, 400L))
    env.fromCollection(data, fields)
  }

  def getTupleContainingPojos(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[(Int, CrazyNested, POJO)]
    data.+=((
      1,
      new CrazyNested("one", "uno", 1L),
      new POJO(1, "First", 10, 100, 1000L, "One", 10000L)))
    data.+=((
      1,
      new CrazyNested("one", "uno", 1L),
      new POJO(1, "First", 10, 100, 1000L, "One", 10000L)))
    data.+=((
      1,
      new CrazyNested("one", "uno", 1L),
      new POJO(1, "First", 10, 100, 1000L, "One", 10000L)))
    data.+=((
      2,
      new CrazyNested("two", "duo", 2L),
      new POJO(1, "First", 10, 100, 1000L, "One", 10000L)))
    env.fromCollection(data, fields)
  }

  def getMixedPojoDataSet(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[POJO]
    data.+=(new POJO(1, "First", 10, 100, 1000L, "One", 10100L))
    data.+=(new POJO(2, "First_", 10, 105, 1000L, "One", 10200L))
    data.+=(new POJO(3, "First", 11, 102, 3000L, "One", 10200L))
    data.+=(new POJO(4, "First_", 11, 106, 1000L, "One", 10300L))
    data.+=(new POJO(5, "First", 11, 102, 2000L, "One", 10100L))
    data.+=(new POJO(6, "Second_", 20, 200, 2000L, "Two", 10100L))
    data.+=(new POJO(7, "Third", 31, 301, 2000L, "Three", 10200L))
    data.+=(new POJO(8, "Third_", 30, 300, 1000L, "Three", 10100L))
    env.fromCollection(data, fields)
  }

  def getSmallTuplebasedDataSetMatchingPojo(env: BatchTableEnvironment, fields: String = null):
  Table = {
    val data = new mutable.MutableList[(Long, Integer, Integer, Long, String, Integer, String)]
    data.+=((10000L, 10, 100, 1000L, "One", 1, "First"))
    data.+=((20000L, 20, 200, 2000L, "Two", 2, "Second"))
    data.+=((30000L, 30, 300, 3000L, "Three", 3, "Third"))
    env.fromCollection(data, fields)
  }

  def getPojoWithMultiplePojos(env: BatchTableEnvironment, fields: String = null): Table = {
    val data = new mutable.MutableList[CollectionBatchExecTable
    .PojoWithMultiplePojos]
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("a", "aa", "b", "bb", 1))
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("b", "bb", "c", "cc", 2))
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("b", "bb", "c", "cc", 2))
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("b", "bb", "c", "cc", 2))
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("d", "dd", "e", "ee", 3))
    data.+=(new CollectionBatchExecTable.PojoWithMultiplePojos("d", "dd", "e", "ee", 3))
    env.fromCollection(data, fields)
  }

  /** Tool converters used to convert string fields to array of [[Expression]]s. **/
  implicit def strToExpressions(fields: String): Array[Expression] = {
    if (fields == null) null else {
      ExpressionParser.parseExpressionList(fields).toArray
    }
  }

  case class MutableTuple3[T1, T2, T3](var _1: T1, var _2: T2, var _3: T3)

  class CustomType(var myInt: Int, var myLong: Long, var myString: String) {
    def this() {
      this(0, 0, "")
    }

    override def toString: String = {
      myInt + "," + myLong + "," + myString
    }
  }

  class POJO(
      var number: Int,
      var str: String,
      var nestedTupleWithCustom: (Int, CustomType),
      var nestedPojo: NestedPojo) {
    def this() {
      this(0, "", null, null)
    }

    def this(i0: Int, s0: String, i1: Int, i2: Int, l0: Long, s1: String, l1: Long) {
      this(i0, s0, (i1, new CustomType(i2, l0, s1)), new NestedPojo(l1))
    }

    override def toString: String = {
      number + " " + str + " " + nestedTupleWithCustom + " " + nestedPojo.longNumber
    }

    @transient var ignoreMe: Long = 1L
  }

  class NestedPojo(var longNumber: Long) {
    def this() {
      this(0)
    }
  }

  class CrazyNested(var nest_Lvl1: CrazyNestedL1, var something: Long) {
    def this() {
      this(new CrazyNestedL1, 0)
    }

    def this(set: String) {
      this()
      nest_Lvl1 = new CrazyNestedL1
      nest_Lvl1.nest_Lvl2 = new CrazyNestedL2
      nest_Lvl1.nest_Lvl2.nest_Lvl3 = new CrazyNestedL3
      nest_Lvl1.nest_Lvl2.nest_Lvl3.nest_Lvl4 = new CrazyNestedL4
      nest_Lvl1.nest_Lvl2.nest_Lvl3.nest_Lvl4.f1nal = set
    }

    def this(set: String, second: String, s: Long) {
      this(set)
      something = s
      nest_Lvl1.a = second
    }
  }

  class CrazyNestedL1 {
    var a: String = null
    var b: Int = 0
    var nest_Lvl2: CrazyNestedL2 = null
  }

  class CrazyNestedL2 {
    var nest_Lvl3: CrazyNestedL3 = null
  }

  class CrazyNestedL3 {
    var nest_Lvl4: CrazyNestedL4 = null
  }

  class CrazyNestedL4 {
    var f1nal: String = null
  }

  class PojoContainingTupleAndWritable(
      var someInt: Int,
      var someString: String,
      var hadoopFan: IntWritable,
      var theTuple: (Long, Long)) {
    def this() {
      this(0, "", new IntWritable(0), (0, 0))
    }

    def this(i: Int, l1: Long, l2: Long) {
      this()
      hadoopFan = new IntWritable(i)
      someInt = i
      theTuple = (l1, l2)
    }

  }

  class Pojo1 {
    var a: String = null
    var b: String = null

    override def toString = s"Pojo1 a=$a b=$b"
  }

  class Pojo2 {
    var a2: String = null
    var b2: String = null

    override def toString = s"Pojo2 a2=$a2 b2=$b2"
  }

  class PojoWithMultiplePojos {

    def this(a: String, b: String, a1: String, b1: String, i0: Int) {
      this()
      p1 = new Pojo1
      p1.a = a
      p1.b = b
      p2 = new Pojo2
      p2.a2 = a1
      p2.b2 = b1
      this.i0 = i0
    }

    var p1: Pojo1 = null
    var p2: Pojo2 = null
    var i0: Int = 0

    override def toString = s"PojoWithMultiplePojos p1=$p1 p2=$p2 i0=$i0"
  }

}

