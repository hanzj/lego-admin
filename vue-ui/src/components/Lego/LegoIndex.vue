<template>
  <div
    v-empty="formErrorMsg != null || !auth.read"
    xs-empty-icon="nopermission"
    :xs-empty-text="auth.read ? formErrorMsg : '暂无权限'">
    <lego-list-head
      :search.sync="search"
      :menu-code="menuCode"
      :create-fun="createClick"
      :title="auth.title"
      placeholder="请输入名称"
      :main-title="`新建`"
      @on-search="commonSearch"
      @on-export="exportInfos" />
    <div class="lego-container">
      <lego-table-head
        ref="legoTableHead"
        :sort-data="sortData"
        :menu-list="headMenuList"
        :field-list="filterFieldList"
        :form-code="formCode"
        :menu-code="menuCode"
        @filter="handleFilter"
        @handle="actionHandle"
        @scene="handleScene"/>
      <el-table
        v-loading="loading"
        id="lego-table"
        :data="pageList"
        :height="tableHeight"
        :cell-class-name="cellClassName"
        row-key="code"
        class="n-table--border"
        stripe
        border
        highlight-current-row
        @row-click="handleRowClick"
        @sort-change="sortChange"
        @header-dragend="handleHeaderDragend"
        @selection-change="handleSelectionChange">
        <el-table-column
          show-overflow-tooltip
          reserve-selection
          type="selection"
          align="center"
          width="55"/>
        <el-table-column
          v-for="(item, index) in fieldList"
          :key="index"
          :fixed="index==0"
          :prop="item.fieldCode"
          :label="item.name"
          :min-width="item.minWidth"
          :width="item.width"
          sortable="custom"
          show-overflow-tooltip>
          <template slot-scope="{ row, column }">
            <field-view
              :item="item"
              :form-type="item.formType"
              :value="row[column.property]"
              @clickEntity="handleEntityClick" />
          </template>
        </el-table-column>
        <el-table-column
          :resizable="false"
          fixed="right"
          width="40">
          <template slot="header">
            <lego-table-sort
              :form-code="formCode"
              @change="setSave"/>
          </template>
        </el-table-column>
        <empty
          slot="empty"
          :props="{
            buttonTitle: `新建${auth.title}`,
            showButton: auth.add
          }"
          @click="createClick"
        />
      </el-table>
      <div class="p-contianer">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="pageSizes"
          :page-size.sync="pageSize"
          :total="total"
          :pager-count="5"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"/>
      </div>
    </div>
    <lego-all-detail
      :visible.sync="relativeEntity.show"
      :detail-code="relativeEntity.code"
      :form-code="relativeEntity.formCode"
      :page-codes="relativeEntity.pageCodes"
      @handle="actionHandle"
      @hide-view="relativeEntity.show=false"/>
    <!-- 新建 -->
    <lego-all-create
      :visible.sync="createShow"
      :form-code="formCode"
      @close="createShow = false"
      @handle="actionHandle"
    />
  </div>
</template>

<script>
import IndexMixin from './mixins/LegoIndex'

export default {
  name: 'LegoIndex',
  mixins: [IndexMixin],
  data() {
    return {
      createShow: false,
      menuCode: '', // 菜单编码
      unionKey: [], // 表格对象唯一键
      listRequest: {},
      deleteRequest: {},
      exportRequest: {},
      exportAllRequest: {}
    }
  },
  methods: {
    /**
     * 新建点击
     */
    createClick() {
      this.createShow = true
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/lego-table.scss';
</style>
