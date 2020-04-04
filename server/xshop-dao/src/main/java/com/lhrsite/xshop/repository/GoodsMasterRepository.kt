package com.lhrsite.xshop.repository

import com.lhrsite.xshop.po.GoodsMaster
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoodsMasterRepository : JpaRepository<GoodsMaster, String> {

}