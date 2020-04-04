export interface TableSearchData {
  goodsKeyword?: string,
  goodsCategoryId?: number,
  page?: number,
  pageSize?: number
}

export interface GoodsListVO {
  goodsId: string,
  goodsTitle: string,
  // goodsDescribe: string,
  goodsCover: string,
  goodsStatus: number,
  // goodsContent: string,
  // goodsBanner: string,
  updateUser: number,
  updateUserName: string,
  createTime: string,
  updateTime: string,
  categoryId: number
  categoryName: string
}

export interface GoodsMaster {
  goodsId?: string,
  goodsTitle?: string,
  goodsDescribe?: string,
  goodsCover?: string,
  goodsStatus?: number,
  goodsContent?: string,
  goodsBanner?: string,
  updateUser?: number,
  createTime?: string,
  updateTime?: string,
  category1?: number,
  category2?: number
}

export interface GoodsDetail extends GoodsMaster{

}

export interface GoodsCategory {
  categoryId: number,
  categoryFid: number,
  categoryName: string,
  categoryStatus?: number,
  categorySort?: number,
  createTime?: string,
  updateTime?: string,
  children: Array<GoodsCategory>
}
