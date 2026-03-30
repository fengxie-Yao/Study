<template>
  <div class="user-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="queryParams.nickname" placeholder="请输入昵称" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="openDialog()">新增用户</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="nickname" label="昵称" width="150" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column label="角色" width="200">
          <template #default="{ row }">
            <el-tag
              v-for="role in row.roles"
              :key="role"
              size="small"
              style="margin-right: 4px"
              :type="role.includes('ADMIN') ? 'danger' : 'info'"
            >
              {{ role.replace('ROLE_', '') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑 对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" :disabled="!!formData.id" placeholder="仅新增时填写" />
          <div v-if="formData.id" class="form-tip">用户名不可修改</div>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="formData.password" type="password" show-password :placeholder="formData.id ? '不填则保持不变' : '请输入密码'" />
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="formData.nickname" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" />
        </el-form-item>

        <el-form-item label="角色" prop="roles">
          <el-select v-model="formData.roles" multiple placeholder="请选择角色" style="width: 100%">
            <el-option v-for="role in roleOptions" :key="role" :label="role" :value="role" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="enabled">
          <el-switch v-model="formData.enabled" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import { userApi } from '@/api/user';
import type { User, UserQuery, UserSaveForm } from '@/types/user';

// --- 状态定义 ---
const loading = ref(false);
const submitLoading = ref(false);
const tableData = ref<User[]>([]);
const total = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const formRef = ref<FormInstance>();
const roleOptions = ref<string[]>([]);

// 查询参数
const queryParams = reactive<UserQuery>({
  pageNum: 1,
  pageSize: 10,
  username: '',
  nickname: '',
});

// 表单数据
const formData = reactive<UserSaveForm>({
  enabled: true,
  roles: [],
});

// 表单校验规则
const formRules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }],
  roles: [{ required: true, message: '至少选择一个角色', trigger: 'change' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur', validator: (rule, value, callback) => {
        if (!formData.id && !value) {
          callback(new Error('新增用户必须输入密码'));
        } else {
          callback();
        }
      }
    }
  ],
};

// --- 方法定义 ---

// 获取列表
const fetchData = async () => {
  loading.value = true;
  try {
    const res = await userApi.getList(queryParams);
    tableData.value = res.content;
    total.value = res.totalElements;
    // 修正页码：Spring Data Page number 是 0-based，前端是 1-based，这里已经在 API 层或查询参数处理了
    // 如果后端返回 number 是 0，前端 currentPage 应该是 1，通常不需要额外转换，只要分页组件绑定正确即可
  } catch (error) {
    ElMessage.error('获取用户列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索与重置
const handleSearch = () => {
  queryParams.pageNum = 1;
  fetchData();
};

const resetSearch = () => {
  queryParams.username = '';
  queryParams.nickname = '';
  handleSearch();
};

// 打开对话框
const openDialog = (row?: User) => {
  dialogTitle.value = row ? '编辑用户' : '新增用户';
  dialogVisible.value = true;

  if (row) {
    // 填充表单 (注意不要直接引用 row 对象，避免双向绑定污染表格)
    Object.assign(formData, {
      id: row.id,
      username: row.username,
      nickname: row.nickname,
      email: row.email,
      enabled: row.enabled,
      roles: [...row.roles],
      password: '', // 密码清空
    });
  } else {
    // 重置为新增状态
    Object.assign(formData, {
      id: undefined,
      username: '',
      password: '',
      nickname: '',
      email: '',
      enabled: true,
      roles: [],
    });
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true;
      try {
        // 如果是编辑且没填密码，移除 password 字段传给后端 (后端逻辑会忽略)
        const submitData = { ...formData };
        if (submitData.id && !submitData.password) {
          delete submitData.password;
        }

        await userApi.save(submitData);
        ElMessage.success('操作成功');
        dialogVisible.value = false;
        fetchData();
      } catch (error) {
        ElMessage.error('操作失败');
      } finally {
        submitLoading.value = false;
      }
    }
  });
};

// 删除
const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除该用户吗？此操作不可恢复。', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await userApi.delete(id);
      ElMessage.success('删除成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
    }
  });
};

// 重置表单校验
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields();
  }
};

// 加载角色选项
const loadRoles = async () => {
  try {
    roleOptions.value = await userApi.getRoles();
  } catch (e) {
    // 默认给一些值防止报错，或者从常量获取
    roleOptions.value = ['ROLE_ADMIN', 'ROLE_USER'];
  }
};

onMounted(() => {
  fetchData();
  loadRoles();
});
</script>

<style scoped>
.user-manage {
  padding: 20px;
}
.search-card {
  margin-bottom: 20px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}
</style>
