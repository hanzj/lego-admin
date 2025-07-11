<template>
  <div
    :class="`is-${formType}`"
    class="field-view">
    <slot name="leftContent" />
    <el-switch
      v-if="formType == 'boolean'"
      :value="value"
      :active-value="item.activeValue"
      :inactive-value="item.inactiveValue"
      disabled />
    <image-view
      v-else-if="['handwritingSign', 'picture'].includes(formType)"
      :src="value"
      :height="config.signatureHeight" />
    <desc-text
      v-else-if="formType == 'descText'"
      :value="item.defaultValue"
      :disabled="true"/>
    <desc-text
      v-else-if="formType == 'richTextEditor'"
      :value="value"
      :disabled="true"/>
    <span
      v-else-if="formType == 'website'"
      :class="{'can-check': !isEmpty}"
      @click.stop="openUrl(value)" >{{ value }}</span>
    <span
      v-else-if="formType == 'entity'"
      :class="{'can-check': !isEmpty}"
      @click="handleEntityClick(value)" >{{ getCommonShowValue() }}</span>
    <span
      :class="[{'can-check': clickable}, {'can-visit--bold': clickable}]"
      @click="handleClick(value)"
      v-else>{{ getCommonShowValue() }}</span>
    <map-view
      v-if="mapViewShow"
      :title="value.address"
      :lat="value.lat"
      :lng="value.lng"
      @hidden="mapViewShow=false"
    />
  </div>
</template>

<script>
import ImageView from '@/components/Common/ImageView'
import DescText from '@/components/Common/DescText'
import MapView from '@/components/Common/MapView' // 地图详情

import merge from '@/utils/merge'
import { isEmpty } from '@/utils/types'
import { getFormFieldShowValue } from './utils'

const DefaultFieldView = {
  signatureHeight: '26px'
}

export default {
  // 特殊字段展示
  name: 'FieldView',
  components: {
    ImageView,
    DescText,
    MapView
  },
  props: {
    item: Object, // 自定义字段参数信息
    leftContent: Object,
    formType: String,
    value: [String, Object, Array, Number, Boolean]
  },
  data() {
    return {
      // 控制展示地图详情
      mapViewShow: false
    }
  },
  computed: {
    config() {
      return merge({ ...DefaultFieldView }, this.item || {})
    },
    isEmpty() {
      return isEmpty(this.value)
    },
    clickable() {
      if (this.item && this.item.clickable) {
        return true
      }
      return false
    }
  },
  methods: {
    openUrl(url) {
      if (!url.match(/^https?:\/\//i)) {
        url = 'http://' + url
      }
      window.open(url)
    },
    getCommonShowValue() {
      return getFormFieldShowValue(this.formType, this.value)
    },
    handleClick(value) {
      if (this.clickable) {
        this.$emit('clickValue', { field: this.item, value: value })
      }
    },
    handleEntityClick(value) {
      this.$emit('clickEntity', { field: this.item, value: value })
    }
  }
}
</script>

<style lang="scss" scoped>
.field-view {
  overflow: hidden;
  text-overflow: ellipsis;
	.can-check {
		color: $xr-color-primary;
		cursor: pointer;
	}

	&.is-website {
		display: inline;
	}
}
</style>
