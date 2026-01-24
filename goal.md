### 第一阶段：基建与地基 (Environment & Database)
*目标：不依赖代码生成器，手写实体类和数据库配置。*

*   [ ] **初始化项目**
    *   使用 Spring Initializr 创建项目。
    *   引入依赖：Web, JPA, MySQL Driver, Lombok (可选，建议手写 Getter/Setter 找手感), Validation。
*   [ ] **设计并创建数据库表 (SQL)**
    *   不要指望 JPA 自动建表（`ddl-auto: update`），先手动设计 SQL 并在数据库执行，然后再写 Entity 去映射它。这是为了恢复你对 DB 结构的敏感度。
    *   `users` 表：id, username, password, email, nickname, created_at
    *   `posts` 表：id, user_id, title, content, summary, status (DRAFT/PUBLISHED), created_at, updated_at
*   [ ] **搭建基础架构**
    *   创建包结构：`config`, `controller`, `service`, `repository`, `entity`, `dto`, `exception`。
    *   配置 `application.yml` 连接数据库。

---

### 第二阶段：用户与认证 (User & Auth) —— 最难的先做
*目标：重新掌握 Spring Security 过滤器链，而不是复制一段不明觉厉的代码。*

*   [ ] **用户注册接口**
    *   [ ] 定义 `UserRegisterRequest` (DTO)，使用 `@NotBlank`, `@Email`, `@Size` 做参数校验。
    *   [ ] Service 层：检查用户名/邮箱是否重复。
    *   [ ] **重点**：使用 `BCryptPasswordEncoder` 对密码加密后存入数据库。
*   [ ] **全局异常处理 (Global Exception Handling)**
    *   [ ] 手写 `@RestControllerAdvice`。
    *   [ ] 捕获 `MethodArgumentNotValidException`，返回格式统一的 JSON（例如：`{ "code": 400, "msg": "邮箱格式错误" }`）。
*   [ ] **Spring Security 集成 (核心挑战)**
    *   [ ] 实现 `UserDetailsService` 接口，从数据库加载用户信息。
    *   [ ] 配置 `SecurityFilterChain`：
        *   放行 `/api/auth/**` (注册/登录)。
        *   其他接口全部需要认证。
    *   [ ] **登录接口**：实现一个返回 JWT (Json Web Token) 的登录接口，或者使用传统的 Session/Cookie 模式（建议复健先用 Session，简单点，通了再改 JWT）。
*   [ ] **获取当前登录用户信息接口**
    *   [ ] 从 `SecurityContextHolder` 中获取当前用户 ID。

---

### 第三阶段：核心业务 (Core Content)
*目标：熟练掌握 JPA 查询、DTO 转换、以及业务逻辑封装。*

*   [ ] **文章发布 (Create)**
    *   [ ] 入参：标题、内容、简介。
    *   [ ] 逻辑：自动关联当前登录用户（Author），设置创建时间，初始状态为 `PUBLISHED`。
*   [ ] **文章修改 (Update)**
    *   [ ] **权限检查**：这是重点！必须检查**当前登录用户 ID 是否等于文章作者 ID**。如果不等，抛出 `403 Forbidden`。不要相信前端传来的参数，要相信 Session/Token。
    *   [ ] 更新 `updated_at` 时间戳。
*   [ ] **文章列表 (Read)**
    *   [ ] 分页查询：使用 `Pageable` 和 `Page<Post>`。
    *   [ ] 响应数据优化：列表页**不要**返回 `content` (正文) 字段，只返回标题、简介、作者名、发布时间。这需要你手写 DTO 转换逻辑。
*   [ ] **文章详情 (Read)**
    *   [ ] 根据 ID 查询。
    *   [ ] 顺便实现一个简单的 PV (浏览量) +1 逻辑（即使不精准）。
*   [ ] **文章删除 (Delete)**
    *   [ ] 实现**逻辑删除** (Soft Delete)：表中加一个 `is_deleted` 字段，删除时 update 为 1，查询时默认带上 `where is_deleted = 0`。

---

### 第四阶段：进阶关系处理 (Relationships)
*目标：解决 JPA 中著名的 N+1 问题，处理复杂映射。*

*   [ ] **评论功能 (One-to-Many)**
    *   [ ] 创建 `comments` 表：id, post_id, user_id, content, created_at。
    *   [ ] 接口：给指定文章 ID 发表评论。
    *   [ ] 接口：获取指定文章下的所有评论。
*   [ ] **标签系统 (Many-to-Many)**
    *   [ ] 创建 `tags` 表和 `post_tags` 中间表。
    *   [ ] 发布/修改文章时，支持传入 `["Java", "Spring"]` 这样的标签列表。
    *   [ ] 逻辑：如果标签库里有，就关联；没有，就新建标签再关联。
*   [ ] **性能优化 (No AI Challenge)**
    *   [ ] 当你查询文章列表时，JPA 默认可能会针对每一篇文章去单独查询 Author 信息（N+1 问题）。
    *   [ ] **任务**：观察控制台打印的 SQL 语句，使用 `@EntityGraph` 或者 JPQL 的 `JOIN FETCH` 手动优化查询，确保查 10 篇文章只执行 1 条（或 2 条）SQL。

---

### 第五阶段：自我验收 (QA & Testing)
*目标：像个专业工程师一样交付，而不是写完就算。*

*   [ ] **API 测试**
    *   使用 Postman 或 Insomnia，把所有接口跑通。
    *   尝试攻击自己：传空参数、传超长字符串、删除别人的文章，看系统是否会崩或报错信息是否友好。
*   [ ] **手写单元测试 (Unit Test)**
    *   不要写全部，但**必须**为 `PostService` 写测试。
    *   使用 `Mockito` 模拟 Repository，测试“只有作者才能修改文章”这个逻辑是否生效。

---
