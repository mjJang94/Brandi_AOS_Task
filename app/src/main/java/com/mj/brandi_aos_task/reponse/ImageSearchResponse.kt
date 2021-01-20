package com.mj.brandi_aos_task.reponse

class ImageSearchResponse {

    val documents : ArrayList<Info> ?= null

    class Info {
        val collection: String ?= null
        val datetime: String ?= null
        val display_sitename: String ?= null
        val doc_url: String ?= null
        val image_url: String ?= null
        val thumbnail_url: String ?= null
        val height: Int ?= 0
        val width: Int ?= 0

    }


}