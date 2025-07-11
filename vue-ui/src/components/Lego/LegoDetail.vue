<template>
  <div
    v-loading="loading"
    ref="detailMain"
    class="detail-main">
    <flexbox
      v-if="auth.detail"
      direction="column"
      align="stretch"
      class="d-container">
      <lego-detail-head
        :action-types="actionTypes"
        :auth="auth"
        :formCode="formCode"
        :detail="detailData"
        :head-field-list="headFieldList"
        :page-codes="pageCodes"
        @pageChange="pageChange"
        @handle="actionHandle"
        @close="hideView" />
      <flexbox class="d-container-bd" align="stretch">
        <el-tabs
          v-model="tabCurrentName"
          type="border-card"
          class="d-container-bd--left">
          <el-tab-pane
            v-for="(item, index) in tabNames"
            :key="index"
            :label="item.label"
            :name="item.name"
            lazy>
            <component
              :is="item.name"
              :detail-code="detailCode"
              :menu-code="menuCode"
              :form-code="formCode"
              :field-list="fieldList"
              :system-field-list="systemFieldList"
              @handle="actionHandle"
              @clickEntity="handleEntityClick" />
          </el-tab-pane>
        </el-tabs>
        <transition name="slide-fade">
          <el-tabs
            v-show="showImportInfo"
            value="chiefly-contacts"
            type="border-card"
            class="d-container-bd--right">
            <el-tab-pane
              label="重要信息"
              name="chiefly-contacts"
              lazy>
              <import-info :list="importList" :detail="detailData" class="import-info" />
            </el-tab-pane>
          </el-tabs>
        </transition>
      </flexbox>
    </flexbox>
    <!-- 新建 -->
    <lego-all-create
      :visible.sync="isCreate"
      :form-code="formCode"
      :action="{type: 'update', detailData: detailData}"
      @close="isCreate = false"
      @handle="actionHandle"
    />
    <div v-show="relativeEntity.show" class="full-container">
      <lego-all-detail
        :visible.sync="relativeEntity.show"
        :detail-code.sync="relativeEntity.code"
        :form-code="relativeEntity.formCode"
        @hide-view="closeEntityDetail()"/>
    </div>
  </div>
</template>

<script>
import DetailMixin from './mixins/LegoDetail'
import RelativeHandle from '@/components/Relative/RelativeHandle'
import RelativeFile from '@/components/Relative/RelativeFile'
import RelativePrint from '@/components/Relative/RelativePrint'

export default {
  name: 'LegoDetail',
  components: {
    RelativeHandle,
    RelativeFile,
    RelativePrint
  },
  mixins: [DetailMixin],
  data() {
    return {
      updateRequest: {},
      detailRequest: {},
      deleteRequest: {}
    }
  },
  computed: {
    /** 明细tab页 */
    tabNames() {
      return [
        { label: '详细资料', name: 'LegoEditBaseInfo' },
        { label: '操作日志', name: 'RelativeHandle' },
        { label: '附件', name: 'RelativeFile' },
        { label: '打印记录', name: 'RelativePrint' }
      ]
    },
    /** 更多操作 */
    actionTypes() {
      return [
        { name: '删除', type: 'delete', icon: 'delete' }
      ]
    }
  },
  methods: {
    /** 更多操作事件回调 */
    doActionHandler(data) {
      if (data.type === 'delete') {
        this.$confirm('此操作将永久删除[' + this.detailCode + ']，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.deleteRequest([this.detailCode]).then(res => {
            this.$emit('handle', { type: 'save-success' })
            this.hideView()
            loading.close()
          })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/detail.scss';
</style>
