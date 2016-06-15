/**
 * Copyright 2016 Zhang.Jingwei (basecn@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kv.cached.db.server.model.beans;


/**
 *
 */
public class KvTableObject {

	private String[] partitions;

	private String keyType = "normal";

	private String valueType;

	private String valueTypeInfo;

	private int maxValueLength = 0;

	private boolean compressed = true;

	private String compression;

	private boolean encrypted = false;

	private String encryption;

	private String encryptionInfo;

	private boolean enableSlice = true;

	private int sliceCount = 0;

	private long dataAlive = 0;

	private int dataKtiCount = 10;


	/**
	 * 
	 */
	public KvTableObject() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the partitions
	 */
	public final String[] getPartitions() {
		return partitions;
	}


	/**
	 * @param partitions the partitions to set
	 */
	public final void setPartitions(String[] partitions) {
		this.partitions = partitions;
	}


	/**
	 * @return the keyType
	 */
	public final String getKeyType() {
		return keyType;
	}


	/**
	 * @param keyType the keyType to set
	 */
	public final void setKeyType(String keyType) {
		this.keyType = keyType;
	}


	/**
	 * @return the valueType
	 */
	public final String getValueType() {
		return valueType;
	}


	/**
	 * @param valueType the valueType to set
	 */
	public final void setValueType(String valueType) {
		this.valueType = valueType;
	}


	/**
	 * @return the valueTypeInfo
	 */
	public final String getValueTypeInfo() {
		return valueTypeInfo;
	}


	/**
	 * @param valueTypeInfo the valueTypeInfo to set
	 */
	public final void setValueTypeInfo(String valueTypeInfo) {
		this.valueTypeInfo = valueTypeInfo;
	}


	/**
	 * @return the maxValueLength
	 */
	public final int getMaxValueLength() {
		return maxValueLength;
	}


	/**
	 * @param maxValueLength the maxValueLength to set
	 */
	public final void setMaxValueLength(int maxValueLength) {
		this.maxValueLength = maxValueLength;
	}


	/**
	 * @return the compressed
	 */
	public final boolean isCompressed() {
		return compressed;
	}


	/**
	 * @param compressed the compressed to set
	 */
	public final void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}


	/**
	 * @return the compression
	 */
	public final String getCompression() {
		return compression;
	}


	/**
	 * @param compression the compression to set
	 */
	public final void setCompression(String compression) {
		this.compression = compression;
	}


	/**
	 * @return the encrypted
	 */
	public final boolean isEncrypted() {
		return encrypted;
	}


	/**
	 * @param encrypted the encrypted to set
	 */
	public final void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}


	/**
	 * @return the encryption
	 */
	public final String getEncryption() {
		return encryption;
	}


	/**
	 * @param encryption the encryption to set
	 */
	public final void setEncryption(String encryption) {
		this.encryption = encryption;
	}


	/**
	 * @return the encryptionInfo
	 */
	public final String getEncryptionInfo() {
		return encryptionInfo;
	}


	/**
	 * @param encryptionInfo the encryptionInfo to set
	 */
	public final void setEncryptionInfo(String encryptionInfo) {
		this.encryptionInfo = encryptionInfo;
	}


	/**
	 * @return the enableSlice
	 */
	public final boolean isEnableSlice() {
		return enableSlice;
	}


	/**
	 * @param enableSlice the enableSlice to set
	 */
	public final void setEnableSlice(boolean enableSlice) {
		this.enableSlice = enableSlice;
	}


	/**
	 * @return the sliceCount
	 */
	public final int getSliceCount() {
		return sliceCount;
	}


	/**
	 * @param sliceCount the sliceCount to set
	 */
	public final void setSliceCount(int sliceCount) {
		this.sliceCount = sliceCount;
	}


	/**
	 * @return the dataAlive
	 */
	public final long getDataAlive() {
		return dataAlive;
	}


	/**
	 * @param dataAlive the dataAlive to set
	 */
	public final void setDataAlive(long dataAlive) {
		this.dataAlive = dataAlive;
	}


	/**
	 * @return the dataKtiCount
	 */
	public final int getDataKtiCount() {
		return dataKtiCount;
	}


	/**
	 * @param dataKtiCount the dataKtiCount to set
	 */
	public final void setDataKtiCount(int dataKtiCount) {
		this.dataKtiCount = dataKtiCount;
	}

}
