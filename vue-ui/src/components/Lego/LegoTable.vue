<template>
  <div>
    <el-table
      v-loading="loading"
      ref="legoTable"
      :data="dataList"
      :height="height"
      :row-key="multiple ? 'code' : ''"
      highlight-current-row
      style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column
        v-if="multiple"
        show-overflow-tooltip
        reserve-selection
        type="selection"
        align="center"
        width="55"/>
      <template v-for="(fields, r) in fieldList">
        <template v-for="(item, index) in fields">
          <el-table-column
            v-if="showColumn(item)"
            :key="r + '-' + index"
            :min-width="item.width"
            :prop="item.fieldCode"
            :label="item.name"
            show-overflow-tooltip>
            <template slot-scope="{ row }">
              <slot name="column" v-bind="{ row: row, item: item }">
                <field-view
                  :item="item"
                  :form-type="item.formType"
                  :value="row[item.fieldCode]"
                  @clickValue="handleField($event, row)" />
              </slot>
            </template>
          </el-table-column>
        </template>
      </template>
      <el-table-column
        v-if="operational"
        fixed="right"
        label="操作"
        :width="editButtonWidth">
        <template slot-scope="scope">
          <slot :row="scope.row" />
        </template>
      </el-table-column>
    </el-table>
    <div class="p-contianer">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        :total="total"
        :pager-count="5"
        class="p-bar"
        background
        layout="prev, pager, next, sizes, total, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </div>
  </div>
</template>

<script>
import FieldView from '@/components/Common/Form/FieldView'
import { isBoolean } from '@/utils/types'

export default {
  name: 'CustomField',
  components: {
    FieldView
  },
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    operational: {
      type: Boolean,
      default: true
    },
    multiple: {
      type: Boolean,
      default: false
    },
    dataList: Array,
    fieldList: Array,
    editButtonWidth: {
      type: Number,
      default: 200
    },
    tableHeighOverly: {
      type: Number,
      default: 0
    },
    currentPage: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 15
    },
    total: {
      type: Number,
      default: 15
    },
    selectedCodes: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      tablePageSize: 15,
      tableCurrentPage: 1,
      tableHeight: document.documentElement.clientHeight - 165, // 表的高度
      pageSizes: [15, 30, 60, 100]
    }
  },
  watch: {
    dataList: {
      handler(val) {
        if (val && val.length > 0) {
          this.handleInitSelect()
        }
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    height() {
      return this.tableHeight + this.tableHeighOverly
    }
  },
  created() {
    window.onresize = () => {
      self.tableHeight = document.documentElement.clientHeight - 140
    }
    this.tablePageSize = this.pageSize
    this.tableCurrentPage = this.currentPage
  },
  methods: {
    showColumn(item) {
      if (isBoolean(item.visible)) {
        return item.visible
      }
      return true
    },
    getList() {
      this.$emit('onList', this.tablePageSize, this.tableCurrentPage)
    },
    handleSizeChange(val) {
      this.tablePageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.tableCurrentPage = val
      this.getList()
    },
    handleField(data, row) {
      this.$emit('onClickField', data, row)
    },
    handleSelectionChange(val) {
      this.$emit('onSelectionChange', val)
    },
    handleClearSelection() {
      this.$refs.legoTable.clearSelection()
    },
    handleInitSelect() {
      if (this.multiple && this.selectedCodes.length > 0) {
        this.$nextTick(() => {
          this.dataList.forEach(data => {
            if (this.selectedCodes.includes(data.code)) {
              this.$refs.legoTable.toggleRowSelection(data, true)
            }
          })
        })
      }
    }
  }
}
</script>
