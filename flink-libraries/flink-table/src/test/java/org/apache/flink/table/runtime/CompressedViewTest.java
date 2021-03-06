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

package org.apache.flink.table.runtime;

import org.apache.flink.api.common.io.blockcompression.BlockCompressionFactory;
import org.apache.flink.api.common.io.blockcompression.BlockCompressionFactoryLoader;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.io.disk.iomanager.BufferFileWriter;
import org.apache.flink.runtime.io.disk.iomanager.FileIOChannel;
import org.apache.flink.runtime.io.disk.iomanager.IOManager;
import org.apache.flink.runtime.io.disk.iomanager.IOManagerAsync;
import org.apache.flink.table.runtime.util.CompressedHeaderlessChannelReaderInputView;
import org.apache.flink.table.runtime.util.CompressedHeaderlessChannelWriterOutputView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CompressedHeaderlessChannelReaderInputView} and
 * {@link CompressedHeaderlessChannelWriterOutputView}.
 */
@RunWith(Parameterized.class)
public class CompressedViewTest {
	private static final int BUFFER_SIZE = 256;

	private IOManager ioManager;

	private BlockCompressionFactory compressionFactory = BlockCompressionFactoryLoader.createBlockCompressionFactory(
		"lz4", new Configuration());

	public CompressedViewTest(boolean useBufferedIO) {
		ioManager = useBufferedIO ? new IOManagerAsync(1024 * 1024, 1024 * 1024) : new IOManagerAsync();
	}

	@Parameterized.Parameters(name = "useBufferedIO-{0}")
	public static Collection<Boolean> parameters() {
		return Arrays.asList(true, false);
	}

	@After
	public void afterTest() {
		this.ioManager.shutdown();
		if (!this.ioManager.isProperlyShutDown()) {
			Assert.fail("I/O Manager was not properly shut down.");
		}
	}

	@Test
	public void testCompressedView() throws IOException {
		for (int testTime = 0; testTime < 10; testTime++) {
			int testRounds = new Random().nextInt(20000);
			FileIOChannel.ID channel = ioManager.createChannel();
			BufferFileWriter writer = this.ioManager.createBufferFileWriter(channel);
			CompressedHeaderlessChannelWriterOutputView outputView =
					new CompressedHeaderlessChannelWriterOutputView(writer, compressionFactory, BUFFER_SIZE);

			for (int i = 0; i < testRounds; i++) {
				outputView.writeInt(i);
			}
			outputView.close();
			int blockCount = outputView.getBlockCount();

			CompressedHeaderlessChannelReaderInputView inputView =
					new CompressedHeaderlessChannelReaderInputView(channel, ioManager, compressionFactory, BUFFER_SIZE, blockCount);

			for (int i = 0; i < testRounds; i++) {
				assertEquals(i, inputView.readInt());
			}
			inputView.close();
		}
	}
}
