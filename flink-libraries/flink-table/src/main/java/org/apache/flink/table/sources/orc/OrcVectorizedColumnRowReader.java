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

package org.apache.flink.table.sources.orc;

import org.apache.flink.table.api.types.InternalType;
import org.apache.flink.table.dataformat.ColumnarRow;

import java.io.IOException;

/**
 * This reader is used to read a {@link ColumnarRow} from input split.
 */
public class OrcVectorizedColumnRowReader extends OrcVectorizedReader {
	/**
	 * the index of current row which will be returned.
	 */
	private int rowIdx;
	/**
	 * the size of current accessible batch.
	 */
	private int batchSize;

	private ColumnarRow columnarRow = new ColumnarRow();

	public OrcVectorizedColumnRowReader(InternalType[] fieldTypes, String[] fieldNames, String[] schemaFieldNames) {
		this(fieldTypes, fieldNames, schemaFieldNames, false);
	}

	public OrcVectorizedColumnRowReader(InternalType[] fieldTypes, String[] fieldNames, String[] schemaFieldNames, boolean copyToFlink) {
		super(fieldTypes, fieldNames, schemaFieldNames, copyToFlink, true);
		this.batchSize = 0;
		this.rowIdx = -1;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		rowIdx++;
		if (rowIdx >= batchSize) {
			if (!nextBatch()) {
				return false;
			} else {
				batchSize = columnarBatch.getNumRows();
				rowIdx = 0;
				columnarRow.setVectorizedColumnBatch(columnarBatch);
			}
		}
		return true;
	}

	@Override
	public Object getCurrentValue() throws IOException, InterruptedException {
		columnarRow.setRowId(rowIdx);
		return columnarRow;
	}
}
