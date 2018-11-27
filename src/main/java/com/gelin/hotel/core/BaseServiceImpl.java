package com.gelin.hotel.core;/**
 * Created by vetech on 2018/11/27.
 */

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.MapUtils;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 方法类型为保护型只允许子类使用，防止 Rest 穿透服务层直接调用数据库的操作
 * （ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author zoujiming
 * @since 2018/11/27
 */
public abstract class BaseServiceImpl <M extends BaseMapper<T>,T>{

    /**
     * 日志记录工具
     */
    private static final Log logger = LogFactory.getLog(BaseServiceImpl.class);

    /**
     * 批量操作是默认处理30条数据后刷新一次缓存
     */
    private static final Integer BATCH_SIZE = 30;

    /**
     * mybatise mapper 对象
     */
    @Autowired
    protected M baseMapper;

    /**
     * <p>
     * 判断数据库操作是否成功
     * </p>
     * <p>
     * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
     * </p>
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    /**
     * 数据库新增实体
     *
     * @param entity 插入实体
     * @return 返回插入是否成功
     */
    protected boolean insert(T entity) {
        return retBool(baseMapper.insert(entity));
    }

    /**
     * 数据库新增实体（不滤掉空字段)
     *
     * @param entity 插入实体
     * @return 返回插入是否成功
     */
    protected boolean insertAllColumn(T entity) {
        return retBool(baseMapper.insertAllColumn(entity));
    }

    /**
     * 数据库批量新增
     *
     * @param entityList 插入实体列表
     * @return 返回插入是否成功
     */
    protected boolean insertBatch(List<T> entityList) {
        return insertBatch(entityList, BATCH_SIZE);
    }

    /**
     * 批量插入
     *
     * @param entityList 批量插入实体列表
     * @param batchSize  每插入多少条数据刷新一次缓存
     * @return 批量插入是否成功
     */
    protected boolean insertBatch(List<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, entityList.get(i));
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
        }
        return true;
    }

    /**
     * 实体存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean 是否插入或更新成功
     */
    protected boolean insertOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return insert(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
                    return updateById(entity) || insert(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    /**
     * 批量更新或插入实体
     * 实体存在更新记录，否插入一条记录 (默认插入或更新30条数据后刷新一次缓存)
     *
     * @param entityList 插入或更新的实体列表
     * @return 是否操作成功
     */
    protected boolean insertOrUpdateBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, BATCH_SIZE);
    }

    /**
     * 批量更新或插入实体
     * 实体存在更新记录，否插入一条记录
     *
     * @param entityList 插入的实体列表
     * @param batchSize  插入多少条数据刷新一次缓存
     * @return 是否插入成功
     */
    protected boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, true);
    }

    /**
     * 批量更新或插入实体
     * 实体存在更新记录，否插入一条记录 (不滤掉空字段，默认处理30条数据后刷新一次缓存)
     *
     * @param entityList 插入的实体列表
     * @return 是否插入成功
     */
    protected boolean insertOrUpdateAllColumnBatch(List<T> entityList) {
        return insertOrUpdateBatch(entityList, BATCH_SIZE, false);
    }

    /**
     * 批量更新或插入实体
     * 实体存在更新记录，否插入一条记录 (不滤掉空字段)
     *
     * @param entityList 插入或更新的实体列表
     * @param batchSize  处理多少条数据后刷新一次缓存
     * @return 是否操作成功
     */
    protected boolean insertOrUpdateAllColumnBatch(List<T> entityList, int batchSize) {
        return insertOrUpdateBatch(entityList, batchSize, false);
    }

    /**
     * 批量插入修改
     *
     * @param entityList 实体对象列表
     * @param batchSize  批量刷新个数
     * @param selective  是否滤掉空字段
     * @return boolean
     */
    protected boolean insertOrUpdateBatch(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            for (int i = 0; i < size; i++) {
                if (selective) {
                    insertOrUpdate(entityList.get(i));
                } else {
                    insertOrUpdateAllColumn(entityList.get(i));
                }
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertOrUpdateBatch Method. Cause", e);
        }
        return true;
    }

    /**
     * 根据ID删除实体
     *
     * @param id 要删除的实体ID
     * @return 是否删除成功
     */
    protected boolean deleteById(Serializable id) {
        return retBool(baseMapper.deleteById(id));
    }

    /**
     * 通过主键列表 批量删除实体
     *
     * @param idList 要删除实体主键列表
     * @return 是否删除成功
     */
    protected boolean deleteBatchIds(List<? extends Serializable> idList) {
        return retBool(baseMapper.deleteBatchIds(idList));
    }

    /**
     * 通过主键更新实体
     *
     * @param entity 要更新的实体
     * @return 是否更新成功
     */
    protected boolean updateById(T entity) {
        return retBool(baseMapper.updateById(entity));
    }

    /**
     * 通过主键更新实体（不滤掉空字段)
     *
     * @param entity 要更新的实体
     * @return 是否更新成功
     */
    protected boolean updateAllColumnById(T entity) {
        return retBool(baseMapper.updateAllColumnById(entity));
    }


    /**
     * 通过主键 批量更新实体 （每更新30条数据刷新一次缓存)
     *
     * @param entityList 要更新的实体列表
     * @return 是否更新成功
     */
    protected boolean updateBatchById(List<T> entityList) {
        return updateBatchById(entityList, BATCH_SIZE);
    }

    /**
     * 通过主键 批量更新实体
     *
     * @param entityList 要更新的实体列表
     * @param batchSize  更新多少条数据刷新一次缓存
     * @return 是否更新成功
     */
    protected boolean updateBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, true);
    }

    /**
     * 通过主键 批量更新实体 （每更新30条数据刷新一次缓存 不过滤空字段)
     *
     * @param entityList 要更新的实体列表
     * @return 是否更新成功
     */
    protected boolean updateAllColumnBatchById(List<T> entityList) {
        return updateAllColumnBatchById(entityList, BATCH_SIZE);
    }

    /**
     * 通过主键 批量更新实体 （不过滤空字段)
     *
     * @param entityList 要更新的实体列表
     * @param batchSize  更新多少条数据刷新一次缓存
     * @return 是否更新成功
     */
    protected boolean updateAllColumnBatchById(List<T> entityList, int batchSize) {
        return updateBatchById(entityList, batchSize, false);
    }

    /**
     * 根据主键ID进行批量修改
     *
     * @param entityList 实体对象列表
     * @param batchSize  批量刷新个数
     * @param selective  是否滤掉空字段
     * @return boolean
     */
    protected boolean updateBatchById(List<T> entityList, int batchSize, boolean selective) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int size = entityList.size();
            SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
            String sqlStatement = sqlStatement(sqlMethod);
            for (int i = 0; i < size; i++) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put("et", entityList.get(i));
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", e);
        }
        return true;
    }

    /**
     * 通过主键 查询实体
     *
     * @param id 要查询的实体主键
     * @return 查询的实体类
     */
    protected T selectById(Serializable id) {
        return baseMapper.selectById(id);
    }

    /**
     * 通过主键列表 查询实体列表
     *
     * @param idList 要查询的实体主键列表
     * @return 查询的实体类列表
     */
    protected List<T> selectBatchIds(List<? extends Serializable> idList) {
        return baseMapper.selectBatchIds(idList);
    }

    /**
     * 通过map 查询实体列表
     *
     * @param columnMap 要查询的实体属性值map
     * @return 查询的实体类列表
     */
    protected List<T> selectByMap(Map<String, Object> columnMap) {
        return baseMapper.selectByMap(columnMap);
    }

    /**
     * 通过查询条件 查询实体
     *
     * @param wrapper 查询条件
     * @return 查询的实体类列表
     */
    protected T selectOne(Wrapper<T> wrapper) {
        return SqlHelper.getObject(baseMapper.selectList(wrapper));
    }

    /**
     * 根据查询条件查询实体数
     *
     * @param wrapper 查询条件
     * @return 满足查询条件实体数
     */
    protected int selectCount(Wrapper<T> wrapper) {
        return SqlHelper.retCount(baseMapper.selectCount(wrapper));
    }

    /**
     * 根据查询条件查询实体列表
     *
     * @param wrapper 查询条件
     * @return 满足查询条件的实体列表
     */
    protected List<T> selectList(Wrapper<T> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    /**
     * 分页查询实体
     *
     * @param page 分页信息
     * @return 查询到的分页结果
     */
    protected Page<T> selectPage(Page<T> page) {
        return selectPage(page, Condition.EMPTY);
    }

    /**
     * 根据查询条件分页查询实体
     *
     * @param page    分页信息
     * @param wrapper 查询条件
     * @return 满足查询条件的实体分页结果
     */
    protected Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
        SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectPage(page, wrapper));
        return page;
    }


    /**
     * 实体存在更新记录，否插入一条记录 (不滤掉空字段)
     *
     * @param entity 实体对象
     * @return boolean 是否插入或更新成功
     */
    protected boolean insertOrUpdateAllColumn(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return insertAllColumn(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
                    return updateAllColumnById(entity) || insertAllColumn(entity);
                }
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    /**
     * 获取当前模型的类型
     *
     * @return 模型的类型
     */
    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(getClass(), 1);
    }

    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     *
     * @return sql 会话信息
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod sql 方法
     * @return sql 语句
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }
}
