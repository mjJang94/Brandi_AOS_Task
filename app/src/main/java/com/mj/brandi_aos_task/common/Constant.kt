package com.mj.brandi_aos_task.common

class Constant{
    companion object {
        //빈값
        const val EMPTY = ""
        //첫번째 값
        const val DEFAULT_SORT = "1"
        //정확도순 검색
        const val ACCURACY = "accuracy"
        //최신순 검색
        const val RECENCY = "recency"
        //페이지당 문서 수 조건 반영
        const val STATIC_SIZE = "30"
        //Kakao Host
        const val KAKAO_SEARCH_HOST = "https://dapi.kakao.com/"
        //Kakao auth value
        const val KAKAO_AUTH_VALUE = "KakaoAK ab42e5264c116c1eea7ace8f54493b69"

        //원본 이미지 url
        const val IMAGE_URL = "IMAGE_URL"
        //이미지 출처
        const val DISPLAY_SITENAME = "DISPLAY_SITENAME"
        //문서 작성시간
        const val DATETIME = "DATETIME"

        //error body
        const val ERRORTYPE = "errorType"
        const val ERRORMESSAGE = "message"
    }
}
