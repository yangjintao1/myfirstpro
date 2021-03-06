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

package org.apache.flink.api.common.typeutils.base;

import org.apache.flink.api.common.functions.Comparator;
import org.apache.flink.api.common.typeutils.CompatibilityResult;
import org.apache.flink.api.common.typeutils.CompatibilityUtil;
import org.apache.flink.api.common.typeutils.TypeDeserializerAdapter;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.common.typeutils.TypeSerializerConfigSnapshot;
import org.apache.flink.api.common.typeutils.UnloadableDummyTypeSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Preconditions;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A serializer for {@link SortedMap}. The serializer relies on a key serializer
 * and a value serializer for the serialization of the map's key-value pairs.
 * It also deploys a comparator to ensure the order of the keys.
 *
 * <p>The serialization format for the map is as follows: four bytes for the
 * length of the map, followed by the serialized representation of each
 * key-value pair. To allow null values, each value is prefixed by a null flag.
 *
 * @param <K> The type of the keys in the map.
 * @param <V> The type of the values in the map.
 */
public final class SortedMapSerializer<K, V> extends AbstractMapSerializer<K, V, SortedMap<K, V>> {

	private static final long serialVersionUID = 1L;

	/** The comparator for the keys in the map. */
	private final Comparator<K> comparator;

	/**
	 * Constructor with given comparator, and the serializers for the keys and
	 * values in the map.
	 *
	 * @param comparator The comparator for the keys in the map.
	 * @param keySerializer The serializer for the keys in the map.
	 * @param valueSerializer The serializer for the values in the map.
	 */
	public SortedMapSerializer(
		Comparator<K> comparator,
		TypeSerializer<K> keySerializer,
		TypeSerializer<V> valueSerializer
	) {
		super(keySerializer, valueSerializer);

		Preconditions.checkNotNull(comparator, "The comparator cannot be null.");
		this.comparator = comparator;
	}

	/**
	 * Returns the comparator for the keys in the map.
	 *
	 * @return The comparator for the keys in the map.
	 */
	public Comparator<K> getComparator() {
		return comparator;
	}

	@Override
	public TypeSerializer<SortedMap<K, V>> duplicate() {
		TypeSerializer<K> keySerializer = getKeySerializer().duplicate();
		TypeSerializer<V> valueSerializer = getValueSerializer().duplicate();

		return new SortedMapSerializer<>(comparator, keySerializer, valueSerializer);
	}

	@Override
	public SortedMap<K, V> createInstance() {
		return new TreeMap<>(comparator);
	}

	@Override
	public boolean canEqual(Object obj) {
		return (obj != null && obj.getClass() == getClass());
	}

	@Override
	public boolean equals(Object o) {
		if (!super.equals(o)) {
			return false;
		}

		SortedMapSerializer<?, ?> that = (SortedMapSerializer<?, ?>) o;
		return comparator.equals(that.comparator);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = result * 31 + comparator.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "SortedMapSerializer{" +
			"comparator = " + comparator +
			", keySerializer = " + keySerializer +
			", valueSerializer = " + valueSerializer +
			"}";
	}

	@Override
	public TypeSerializerConfigSnapshot snapshotConfiguration() {
		return new MapSerializerConfigSnapshot<>(keySerializer, valueSerializer);
	}

	@Override
	public CompatibilityResult<SortedMap<K, V>> ensureCompatibility(
		TypeSerializerConfigSnapshot configSnapshot
	) {
		if (configSnapshot instanceof MapSerializerConfigSnapshot) {
			List<Tuple2<TypeSerializer<?>, TypeSerializerConfigSnapshot>> previousKvSerializersAndConfigs =
				((MapSerializerConfigSnapshot) configSnapshot).getNestedSerializersAndConfigs();

			CompatibilityResult<K> keyCompatResult = CompatibilityUtil.resolveCompatibilityResult(
				previousKvSerializersAndConfigs.get(0).f0,
				UnloadableDummyTypeSerializer.class,
				previousKvSerializersAndConfigs.get(0).f1,
				keySerializer);

			CompatibilityResult<V> valueCompatResult = CompatibilityUtil.resolveCompatibilityResult(
				previousKvSerializersAndConfigs.get(1).f0,
				UnloadableDummyTypeSerializer.class,
				previousKvSerializersAndConfigs.get(1).f1,
				valueSerializer);

			if (!keyCompatResult.isRequiresMigration() && !valueCompatResult.isRequiresMigration()) {
				return CompatibilityResult.compatible();
			} else if (keyCompatResult.getConvertDeserializer() != null && valueCompatResult.getConvertDeserializer() != null) {
				return CompatibilityResult.requiresMigration(
					new SortedMapSerializer<>(
						comparator,
						new TypeDeserializerAdapter<>(keyCompatResult.getConvertDeserializer()),
						new TypeDeserializerAdapter<>(valueCompatResult.getConvertDeserializer())));
			}
		}

		return CompatibilityResult.requiresMigration();
	}
}
