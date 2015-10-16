/**
 * 所有的audit里的对象都不是针对某一个管理员/用户设计的，所以所有的audit对象都是global的形式
 *
 * 在mysql里sharding之后还可以sort的原因是sharding的方案是按照userId sharding, 在每一个shard
 * 里某一特定的user的数据是全部包含的，所以可以完成sort。引申到mongodb里，我们需要把同一用户需要
 * sort 的数据放到一个shard里.
 *
 * 那么，audit是全站的，所以是一个global(unsharding)的collection
 * Created by Lookis on 5/27/15.
 */
package com.zanshang.models.audit;