package com.yapp.yapp.user.domain

import org.springframework.data.jpa.repository.JpaRepository

interface DeletedUserRepository : JpaRepository<DeletedUser, Long>
