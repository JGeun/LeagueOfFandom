package com.soma.lof.common.repository

import com.soma.lof.core_model.dto.CommonItemResponse
import javax.inject.Singleton

@Singleton
interface HomeRepository {
    suspend fun getMainPage(jwtToken: String) : List<CommonItemResponse>
}