package com.mj.daum_image_search.reponse

class ImageSearchResponse {

    val errorType: String ?= null
    val message: String ?= null

    val documents : ArrayList<Info> ?= null
    val meta: NoResultInfo ?= null

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

    class NoResultInfo{
        val is_end: Boolean ?= false
        val pageable_count: Int ?= 0
        val total_count: Int ?= 0

    }


}