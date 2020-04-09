
export interface GoodsCategoryVO {
  categoryId?: Int,
  categoryFid: Int,
  categoryName: String,
  categoryStatus: Int,
  categorySort: Int,
  createTime?: Timestamp,
  updateTime?: Timestamp,
  children: Array<GoodsCategoryVO>
}

