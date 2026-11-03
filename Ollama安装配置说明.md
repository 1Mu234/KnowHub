# Ollama 安装与配置说明（可选 · 本地大模型方案）

> ⚠️ KnowHub **默认已使用硅基流动云端免费 API**（见使用手册 6.1），无需安装 Ollama。
> 本文档仅作为**替代方案**：如果你想完全离线、不依赖云端，可改用本地 Ollama。

> 用于 KnowHub 智能问答功能的本地大模型，完全免费、无需 API Key。

---

## 一、Ollama 是什么

Ollama 是一个本地大模型运行工具，装好后可以在自己电脑上跑开源大模型（如 Qwen、Llama、DeepSeek 等）。
- ✅ 完全免费
- ✅ 不需要 API Key
- ✅ 数据不上传，隐私安全
- ✅ 提供 OpenAI 兼容接口（`/v1/chat/completions`），KnowHub 直接对接

---

## 二、安装步骤

### 2.1 下载安装包

官方下载页：https://ollama.com/download/windows

直接下载安装程序：`OllamaSetup.exe`（约 400MB）

> 如果官网下载慢，可以用镜像：https://ollama.com/download 选 Windows。

### 2.2 安装

双击 `OllamaSetup.exe`，一路 Next 即可。安装路径默认：

```
C:\Users\你的用户名\AppData\Local\Programs\Ollama
```

安装完成后，Ollama 会**自动在后台启动**（系统托盘有羊驼图标）。

### 2.3 验证安装

打开新的 PowerShell 窗口，执行：

```powershell
ollama --version
```

看到版本号（如 `ollama version 0.5.x`）即安装成功。

---

## 三、拉取模型

KnowHub 默认使用 `qwen2.5:7b`（阿里通义千问 2.5，7B 参数，中文友好，速度快）。

### 3.1 拉取模型

```powershell
ollama pull qwen2.5:7b
```

下载约 **4.7GB**，取决于网速。下载完成后自动就绪。

> ⚠️ 如果下载慢，可以设置国内镜像加速：
> ```powershell
> $env:OLLAMA_HOST="0.0.0.0"
> # 临时用镜像拉取（任选其一）
> $env:OLLAMA_MODELS="$env:USERPROFILE\.ollama\models"
> ```
> 或使用国内镜像站：`https://ollama.anhao.org` / `https://ollama.zhongsou.cloud`

### 3.2 验证模型

```powershell
ollama list
```

应能看到 `qwen2.5:7b` 已安装。

### 3.3 快速测试

```powershell
ollama run qwen2.5:7b "你好，请用一句话介绍你自己"
```

看到回答即说明模型正常工作。

---

## 四、服务与端口

Ollama 安装后默认：
- 服务地址：`http://localhost:11434`
- 后台自动启动（开机自启）

验证服务是否在跑：

```powershell
Invoke-WebRequest -Uri "http://localhost:11434/api/tags" -UseBasicParsing
```

返回 200 + JSON 即可。

如果服务没启动，手动启动：
```powershell
ollama serve
```
（这条命令会占用当前终端，保持窗口打开即可；Ctrl+C 停止）

---

## 五、KnowHub 对接配置

KnowHub 默认就配好了 Ollama，**不需要改任何代码**。

确认 `src/main/resources/ai.properties` 内容：

```properties
ai.baseUrl=http://localhost:11434/v1/chat/completions
ai.apiKey=
ai.model=qwen2.5:7b
```

三个要点：
1. `ai.baseUrl` 指向本地 Ollama
2. `ai.apiKey` **留空**（Ollama 不需要 Key）
3. `ai.model` 是你 `ollama pull` 过的模型名

配置正确后，启动 KnowHub，进入「🤖 智能问答」提问即可。

---

## 六、模型选型建议

| 模型 | 大小 | 中文 | 速度 | 适用场景 |
|---|---|---|---|---|
| `qwen2.5:7b` | 4.7GB | ✅ 好 | 快 | 通用问答（推荐） |
| `qwen2.5:3b` | 2.0GB | ✅ 较好 | 很快 | 低配电脑 |
| `qwen2.5:14b` | 9.0GB | ✅ 很好 | 中 | 高质量回答（需 16GB+ 内存） |
| `llama3.1:8b` | 4.7GB | 一般 | 快 | 英文场景 |
| `deepseek-r1:7b` | 4.7GB | ✅ | 中 | 推理能力强 |

> 换模型只需改 `ai.properties` 的 `ai.model`，再 `ollama pull 新模型名` 即可。

---

## 七、常见问题

**Q：`ollama` 命令找不到？**
A：关掉所有 PowerShell 窗口重开，安装程序会自动加 PATH。

**Q：`ollama pull` 下载很慢/失败？**
A：设置镜像：
```powershell
# 临时（当前会话有效）
$env:OLLAMA_HOST="0.0.0.0"
# 用镜像站拉取
ollama pull ollama.anhao.org/qwen2.5:7b
```
然后重命名：`ollama cp ollama.anhao.org/qwen2.5:7b qwen2.5:7b`

**Q：模型加载慢，每次问答要等很久？**
A：Ollama 默认模型加载后会在内存保留 5 分钟，连续问答很快。首次加载慢是正常的（从磁盘读入内存）。可以延长保留时间：
```powershell
$env:OLLAMA_KEEP_ALIVE="30m"
```

**Q：内存不够，模型跑不动？**
A：7B 模型建议至少 8GB 内存（推荐 16GB）。内存不够换 `qwen2.5:3b`。

**Q：端口 11434 被占用？**
A：改 Ollama 端口：
```powershell
$env:OLLAMA_HOST="127.0.0.1:11435"
ollama serve
```
同时把 `ai.properties` 的 `ai.baseUrl` 改成 `http://localhost:11435/v1/chat/completions`。

**Q：不想用本地模型，想用云端大模型？**
A：编辑 `ai.properties`，取消注释云端示例并填你的 Key：
```properties
ai.baseUrl=https://api.siliconflow.cn/v1/chat/completions
ai.apiKey=sk-你的Key
ai.model=Qwen/Qwen2.5-7B-Instruct
```
`ai.properties` 已加入 `.gitignore`，不会提交到公开仓库。

---

## 八、卸载

```powershell
# 停止服务
ollama stop

# 控制面板 → 程序和功能 → 卸载 Ollama
# 或运行安装程序自带的卸载

# 删除模型数据（可选，释放磁盘空间）
Remove-Item -Recurse -Force "$env:USERPROFILE\.ollama"
```
