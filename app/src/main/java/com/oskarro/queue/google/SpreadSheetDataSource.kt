package com.oskarro.queue.google

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsRequestInitializer
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse
import com.oskarro.queue.model.Product

class SpreadSheetDataSource {

    // 1
    private val transport = GoogleNetHttpTransport.newTrustedTransport()
    // 2
    private val jacksonFactory = GsonFactory.getDefaultInstance()
    // 3
    private val sheets = Sheets.Builder(transport, jacksonFactory, null).setSheetsRequestInitializer(SheetsRequestInitializer("AIzaSyCdBItklbztMcgzPsu738NMjwYffdoOIp8")).build()
    // 4
    private val getAll = sheets.spreadsheets()
        .values().get("1xeIsOKfrHtC9NvkkXyz5epaHAmuqezqQY8QYgmiJpMg", "A:AH")
    // 5

    fun listAll(): List<Product> =
        getAll.execute().getValues()
            .toRawEntries()
            .map { it.toProduct() }

    fun listAllFake() =
        getAll.execute().getValues()
            .toRawEntries()

    private fun List<List<Any>>.toRawEntries(): List<Map<String, Any>> {
        val headers = first().map { it as String }           // 1
        val rows = drop(1)                                   // 2
        return rows.map { row -> headers.zip(row).toMap() }  // 3
    }

    private fun Map<String, Any>.stringFor(key: String) = (this[key] as? String) ?: ""
    private fun Map<String, Any>.longFor(key: String) = stringFor(key).toLongOrNull() ?: -1

    private fun Map<String, Any>.toProduct() = Product(
        name = stringFor("Nazwa"),
        orderNumber = stringFor("Index"),
        stage = stringFor("stage")
    )
//
//    val response: BatchGetValuesResponse = sheets.spreadsheets().values()
//        .batchGet(sheetId).setRanges(listOf("items!A3:Z", "siteinfo!A3:Z"))
//        .execute();

}