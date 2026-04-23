/*
 Navicat Premium Dump SQL

 Source Server         : PatientApplication
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : hospital

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 07/01/2026 10:11:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admini
-- ----------------------------
DROP TABLE IF EXISTS `admini`;
CREATE TABLE `admini`  (
  `a_id` int NOT NULL,
  `a_password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `a_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `a_gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `a_card` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `a_phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `a_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`a_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admini
-- ----------------------------
INSERT INTO `admini` VALUES (201701, 'admin', 'admin', '男', '440881199701203551', '18211272779', 'hins555ph@163.com');

-- ----------------------------
-- Table structure for ai_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `ai_chat_message`;
CREATE TABLE `ai_chat_message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `p_id` int NOT NULL COMMENT '患者ID',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息角色（user/assistant/system）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'text' COMMENT '消息类型（text/image）',
  `message_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_patient_id`(`p_id` ASC) USING BTREE,
  INDEX `idx_session_id`(`session_id` ASC) USING BTREE,
  INDEX `idx_message_time`(`message_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_chat_message
-- ----------------------------
INSERT INTO `ai_chat_message` VALUES (1, 2023401022, '[object Object]', 'user', '头疼', 'text', '2026-01-03 07:33:40');
INSERT INTO `ai_chat_message` VALUES (2, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 偏头痛\n2. 紧张性头痛\n3. 高血压\n4. 脑肿瘤\n5. 颈椎病\n\n【推荐科室】神经内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意休息，避免过度劳累。\",\"2. 观察头疼是否伴随有其他症状，如恶心、呕吐、视觉异常等。\",\"3. 避免摄入刺激性食物和饮料，如咖啡、酒精等。\",\"4. 如果头疼持续加重或出现新症状，请及时就医。\",\"5. 就医时，详细描述头疼的性质、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者主要症状为头疼，可能涉及到神经、血管、颈椎等多个方面。根据症状描述，推荐神经内科进行进一步检查。由于头疼的严重程度未知，建议先观察症状，如有必要再及时就医。', 'text', '2026-01-03 07:33:49');
INSERT INTO `ai_chat_message` VALUES (3, 2023401022, '[object Object]', 'user', '胃疼', 'text', '2026-01-03 07:35:40');
INSERT INTO `ai_chat_message` VALUES (4, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• 1. 注意饮食，避免刺激性食物和过冷过热的食物。2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。3. 保持良好的作息习惯，避免过度劳累。4. 如果胃疼持续不缓解或出现加重，及时就医。5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\n\n【推荐理由】\n患者新增胃疼症状，可能与消化系统有关。根据症状，推荐消化内科进行诊断。当前病情严重程度中等，建议先观察症状，暂无需紧急就医。', 'text', '2026-01-03 07:35:47');
INSERT INTO `ai_chat_message` VALUES (5, 2023401022, '[object Object]', 'user', '头疼', 'text', '2026-01-03 07:42:16');
INSERT INTO `ai_chat_message` VALUES (6, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 偏头痛\n2. 紧张性头痛\n3. 高血压\n4. 脑肿瘤\n5. 颈椎病\n\n【推荐科室】神经内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• 1. 重复之前的就医建议，注意休息，避免过度劳累。2. 观察头疼是否出现新的特征，如频率变化、疼痛程度等。3. 如果头疼伴随有胃疼，注意胃疼与头疼之间的关联。4. 避免同时摄入过多刺激性食物和饮料，可能加重头疼和胃疼。5. 如果头疼症状无改善或加剧，应及时就医。\n\n【推荐理由】\n患者再次提到头疼，而之前的症状也包括胃疼。头疼可能与神经系统、血管或颈椎问题有关，推荐患者继续前往神经内科检查。由于头疼的严重程度目前评估为中等，且没有紧急就医的迹象，建议患者先观察症状。', 'text', '2026-01-03 07:42:25');
INSERT INTO `ai_chat_message` VALUES (7, 2023401022, '[object Object]', 'user', '胃疼', 'text', '2026-01-03 07:43:16');
INSERT INTO `ai_chat_message` VALUES (8, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• 1. 注意饮食，避免刺激性食物和过冷过热的食物。2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。3. 保持良好的作息习惯，避免过度劳累。4. 如果胃疼持续不缓解或出现加重，及时就医。5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\n\n【推荐理由】\n患者再次出现胃疼症状，考虑到之前头疼的症状也可能与胃部问题有关（如应激性头痛），因此建议重点考虑消化系统的问题。由于目前病情描述为中等严重程度，且没有紧急就医的迹象，建议患者先观察症状并采取相应的自我管理措施。', 'text', '2026-01-03 07:43:25');
INSERT INTO `ai_chat_message` VALUES (9, 2023401022, '[object Object]', 'user', '胃疼', 'text', '2026-01-03 07:48:37');
INSERT INTO `ai_chat_message` VALUES (10, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者持续出现胃疼症状，考虑到可能与消化系统有关。目前病情描述为中等严重程度，建议患者先观察症状并采取相应的自我管理措施。', 'text', '2026-01-03 07:48:45');
INSERT INTO `ai_chat_message` VALUES (11, 2023401022, '[object Object]', 'user', '肚子疼', 'text', '2026-01-03 07:50:18');
INSERT INTO `ai_chat_message` VALUES (12, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 急性肠胃炎\n2. 肠易激综合征\n3. 胆石症\n4. 胃痉挛\n5. 阑尾炎\n\n【推荐科室】消化内科/普外科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意饮食，避免生冷、油腻和刺激性食物。\",\"2. 观察疼痛是否在排便或排气后有所缓解。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果疼痛持续不缓解或出现加重，应及时就医。\",\"5. 就医时，提供疼痛的具体情况，如疼痛部位、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者出现肚子疼症状，可能与消化系统有关。考虑到之前头疼和胃疼的症状，建议关注消化内科相关疾病。目前病情评估为中等严重程度，建议患者先观察症状并采取相应措施。', 'text', '2026-01-03 07:50:27');
INSERT INTO `ai_chat_message` VALUES (13, 2023401022, '[object Object]', 'user', '胃疼‘', 'text', '2026-01-03 07:52:19');
INSERT INTO `ai_chat_message` VALUES (14, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者多次出现胃疼症状，可能与消化系统有关。根据症状描述，推荐消化内科进行诊断。目前病情严重程度评估为中等，尚无需紧急就医。', 'text', '2026-01-03 07:52:28');
INSERT INTO `ai_chat_message` VALUES (15, 2023401022, '[object Object]', 'user', '胃疼', 'text', '2026-01-03 07:53:12');
INSERT INTO `ai_chat_message` VALUES (16, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者多次出现胃疼症状，可能与消化系统有关。目前病情描述为中等严重程度，建议先观察症状并采取相应的自我管理措施。', 'text', '2026-01-03 07:53:20');
INSERT INTO `ai_chat_message` VALUES (17, 2023401022, '[object Object]', 'user', '腿疼', 'text', '2026-01-03 07:57:02');
INSERT INTO `ai_chat_message` VALUES (18, 2023401022, '[object Object]', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 关节炎\n2. 肌肉拉伤\n3. 坐骨神经痛\n4. 下肢静脉血栓\n5. 骨质疏松\n\n【推荐科室】骨科/神经内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意休息，避免过度使用疼痛的腿部。2. 观察腿疼是否伴随有肿胀、红热或活动受限等症状。3. 可以采取冷敷或热敷的方式缓解疼痛，但避免直接将冰袋敷在皮肤上。4. 如果疼痛持续不缓解或出现加重，请及时就医。5. 就医时，提供腿疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]\n\n【推荐理由】\n患者新增腿疼症状，可能涉及到骨骼、肌肉、神经等多个方面。根据症状，推荐骨科或神经内科进行诊断。目前病情严重程度评估为中等，暂无需紧急就医。', 'text', '2026-01-03 07:57:11');
INSERT INTO `ai_chat_message` VALUES (19, 2023401022, '[object Object]', 'user', '胃疼', 'text', '2026-01-03 08:01:39');
INSERT INTO `ai_chat_message` VALUES (20, 2023401022, '8455d14ff6634b5bac50f36fdbb935bc', 'user', '胃疼', 'text', '2026-01-03 08:05:50');
INSERT INTO `ai_chat_message` VALUES (21, 2023401022, '8455d14ff6634b5bac50f36fdbb935bc', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胃癌\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 注意饮食，避免辛辣、油腻食物，减少烟酒摄入。\",\"2. 观察症状变化，如疼痛持续、加剧或出现其他症状如恶心、呕吐、体重下降等，请及时就医。\",\"3. 可以暂时使用抗酸药缓解疼痛，但不要自行长期服用。\",\"4. 若疼痛无法忍受或出现剧烈疼痛，应立即就医。\",\"5. 就医时请详细告知医生疼痛的性质、时间、频率等。\"]\n\n【推荐理由】\n患者主要症状为胃疼，可能涉及到胃部多种疾病。消化内科是处理此类症状的专业科室，建议关注症状变化，并及时前往该科室就诊。', 'text', '2026-01-03 08:05:59');
INSERT INTO `ai_chat_message` VALUES (22, 2023401022, '9bd9720b06204f0094a7f97c074f863f', 'user', '胃疼', 'text', '2026-01-03 08:08:55');
INSERT INTO `ai_chat_message` VALUES (23, 2023401022, '9bd9720b06204f0094a7f97c074f863f', 'assistant', '根据您的症状，我的分析如下：\n\n【可能的疾病】\n1. 胃炎\n2. 胃溃疡\n3. 胃食管反流病\n4. 胃痉挛\n5. 胆石症\n\n【推荐科室】消化内科\n【严重程度】中等\n【紧急程度】无需紧急就医\n\n【就医建议】\n• [\"1. 避免辛辣、油腻食物，保持饮食清淡。\",\"2. 注意休息，避免过度劳累。\",\"3. 可以暂时服用一些抗酸药缓解症状，如奥美拉唑。\",\"4. 观察症状是否持续或加重，如有加重，请及时就医。\",\"5. 就医时请详细告知医生症状发生的时间、性质、疼痛程度等。\"]\n\n【推荐理由】\n患者症状为胃疼，可能与多种消化系统疾病相关。根据常见病和多发病原则，初步推测以上几种疾病可能性较大。建议暂时观察症状，如症状加重或持续，需及时就医。', 'text', '2026-01-03 08:09:02');

-- ----------------------------
-- Table structure for ai_diagnosis_record
-- ----------------------------
DROP TABLE IF EXISTS `ai_diagnosis_record`;
CREATE TABLE `ai_diagnosis_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `p_id` int NOT NULL COMMENT '患者ID',
  `symptoms` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '症状描述',
  `diseases` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '可能的疾病（逗号分隔）',
  `department` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推荐科室',
  `severity` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '严重程度（轻微/中等/严重/紧急）',
  `need_immediate_care` tinyint(1) NOT NULL COMMENT '是否需要立即就医（0否/1是）',
  `advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '就医建议',
  `reasoning` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '推荐理由',
  `diagnosis_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '诊断时间',
  `session_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_patient_id`(`p_id` ASC) USING BTREE,
  INDEX `idx_session_id`(`session_id` ASC) USING BTREE,
  INDEX `idx_diagnosis_time`(`diagnosis_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI诊断记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_diagnosis_record
-- ----------------------------
INSERT INTO `ai_diagnosis_record` VALUES (1, 2023401022, '头疼', '偏头痛,紧张性头痛,高血压,脑肿瘤,颈椎病', '神经内科', '中等', 0, '[\"1. 注意休息，避免过度劳累。\",\"2. 观察头疼是否伴随有其他症状，如恶心、呕吐、视觉异常等。\",\"3. 避免摄入刺激性食物和饮料，如咖啡、酒精等。\",\"4. 如果头疼持续加重或出现新症状，请及时就医。\",\"5. 就医时，详细描述头疼的性质、发作时间、持续时间等。\"]', '患者主要症状为头疼，可能涉及到神经、血管、颈椎等多个方面。根据症状描述，推荐神经内科进行进一步检查。由于头疼的严重程度未知，建议先观察症状，如有必要再及时就医。', '2026-01-03 07:33:49', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (2, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '1. 注意饮食，避免刺激性食物和过冷过热的食物。2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。3. 保持良好的作息习惯，避免过度劳累。4. 如果胃疼持续不缓解或出现加重，及时就医。5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。', '患者新增胃疼症状，可能与消化系统有关。根据症状，推荐消化内科进行诊断。当前病情严重程度中等，建议先观察症状，暂无需紧急就医。', '2026-01-03 07:35:47', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (3, 2023401022, '头疼', '偏头痛,紧张性头痛,高血压,脑肿瘤,颈椎病', '神经内科', '中等', 0, '1. 重复之前的就医建议，注意休息，避免过度劳累。2. 观察头疼是否出现新的特征，如频率变化、疼痛程度等。3. 如果头疼伴随有胃疼，注意胃疼与头疼之间的关联。4. 避免同时摄入过多刺激性食物和饮料，可能加重头疼和胃疼。5. 如果头疼症状无改善或加剧，应及时就医。', '患者再次提到头疼，而之前的症状也包括胃疼。头疼可能与神经系统、血管或颈椎问题有关，推荐患者继续前往神经内科检查。由于头疼的严重程度目前评估为中等，且没有紧急就医的迹象，建议患者先观察症状。', '2026-01-03 07:42:25', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (4, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '1. 注意饮食，避免刺激性食物和过冷过热的食物。2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。3. 保持良好的作息习惯，避免过度劳累。4. 如果胃疼持续不缓解或出现加重，及时就医。5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。', '患者再次出现胃疼症状，考虑到之前头疼的症状也可能与胃部问题有关（如应激性头痛），因此建议重点考虑消化系统的问题。由于目前病情描述为中等严重程度，且没有紧急就医的迹象，建议患者先观察症状并采取相应的自我管理措施。', '2026-01-03 07:43:25', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (5, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '[\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]', '患者持续出现胃疼症状，考虑到可能与消化系统有关。目前病情描述为中等严重程度，建议患者先观察症状并采取相应的自我管理措施。', '2026-01-03 07:48:45', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (6, 2023401022, '肚子疼', '急性肠胃炎,肠易激综合征,胆石症,胃痉挛,阑尾炎', '消化内科/普外科', '中等', 0, '[\"1. 注意饮食，避免生冷、油腻和刺激性食物。\",\"2. 观察疼痛是否在排便或排气后有所缓解。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果疼痛持续不缓解或出现加重，应及时就医。\",\"5. 就医时，提供疼痛的具体情况，如疼痛部位、发作时间、持续时间等。\"]', '患者出现肚子疼症状，可能与消化系统有关。考虑到之前头疼和胃疼的症状，建议关注消化内科相关疾病。目前病情评估为中等严重程度，建议患者先观察症状并采取相应措施。', '2026-01-03 07:50:27', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (7, 2023401022, '胃疼‘', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '[\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]', '患者多次出现胃疼症状，可能与消化系统有关。根据症状描述，推荐消化内科进行诊断。目前病情严重程度评估为中等，尚无需紧急就医。', '2026-01-03 07:52:28', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (8, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '[\"1. 注意饮食，避免刺激性食物和过冷过热的食物。\",\"2. 观察胃疼是否在饭后出现或加重，以及是否有反酸、嗳气等症状。\",\"3. 保持良好的作息习惯，避免过度劳累。\",\"4. 如果胃疼持续不缓解或出现加重，及时就医。\",\"5. 就医时，提供胃疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]', '患者多次出现胃疼症状，可能与消化系统有关。目前病情描述为中等严重程度，建议先观察症状并采取相应的自我管理措施。', '2026-01-03 07:53:20', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (9, 2023401022, '腿疼', '关节炎,肌肉拉伤,坐骨神经痛,下肢静脉血栓,骨质疏松', '骨科/神经内科', '中等', 0, '[\"1. 注意休息，避免过度使用疼痛的腿部。2. 观察腿疼是否伴随有肿胀、红热或活动受限等症状。3. 可以采取冷敷或热敷的方式缓解疼痛，但避免直接将冰袋敷在皮肤上。4. 如果疼痛持续不缓解或出现加重，请及时就医。5. 就医时，提供腿疼的具体情况，如疼痛部位、发作时间、持续时间等。\"]', '患者新增腿疼症状，可能涉及到骨骼、肌肉、神经等多个方面。根据症状，推荐骨科或神经内科进行诊断。目前病情严重程度评估为中等，暂无需紧急就医。', '2026-01-03 07:57:11', '[object Object]');
INSERT INTO `ai_diagnosis_record` VALUES (10, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胃癌', '消化内科', '中等', 0, '[\"1. 注意饮食，避免辛辣、油腻食物，减少烟酒摄入。\",\"2. 观察症状变化，如疼痛持续、加剧或出现其他症状如恶心、呕吐、体重下降等，请及时就医。\",\"3. 可以暂时使用抗酸药缓解疼痛，但不要自行长期服用。\",\"4. 若疼痛无法忍受或出现剧烈疼痛，应立即就医。\",\"5. 就医时请详细告知医生疼痛的性质、时间、频率等。\"]', '患者主要症状为胃疼，可能涉及到胃部多种疾病。消化内科是处理此类症状的专业科室，建议关注症状变化，并及时前往该科室就诊。', '2026-01-03 08:05:59', '8455d14ff6634b5bac50f36fdbb935bc');
INSERT INTO `ai_diagnosis_record` VALUES (11, 2023401022, '胃疼', '胃炎,胃溃疡,胃食管反流病,胃痉挛,胆石症', '消化内科', '中等', 0, '[\"1. 避免辛辣、油腻食物，保持饮食清淡。\",\"2. 注意休息，避免过度劳累。\",\"3. 可以暂时服用一些抗酸药缓解症状，如奥美拉唑。\",\"4. 观察症状是否持续或加重，如有加重，请及时就医。\",\"5. 就医时请详细告知医生症状发生的时间、性质、疼痛程度等。\"]', '患者症状为胃疼，可能与多种消化系统疾病相关。根据常见病和多发病原则，初步推测以上几种疾病可能性较大。建议暂时观察症状，如症状加重或持续，需及时就医。', '2026-01-03 08:09:02', '9bd9720b06204f0094a7f97c074f863f');

-- ----------------------------
-- Table structure for arrange
-- ----------------------------
DROP TABLE IF EXISTS `arrange`;
CREATE TABLE `arrange`  (
  `ar_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `ar_time` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`ar_id`) USING BTREE,
  INDEX `arTOd`(`d_id` ASC) USING BTREE,
  CONSTRAINT `arTOd` FOREIGN KEY (`d_id`) REFERENCES `doctor` (`d_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of arrange
-- ----------------------------
INSERT INTO `arrange` VALUES ('2019002023-09-18', '2023-09-18', 201900);
INSERT INTO `arrange` VALUES ('2019002023-09-20', '2023-09-20', 201900);
INSERT INTO `arrange` VALUES ('2019002023-09-22', '2023-09-22', 201900);
INSERT INTO `arrange` VALUES ('2019012023-09-14', '2023-09-14', 201901);
INSERT INTO `arrange` VALUES ('2019012023-09-15', '2023-09-15', 201901);
INSERT INTO `arrange` VALUES ('2019012023-09-17', '2023-09-17', 201901);
INSERT INTO `arrange` VALUES ('2019012023-09-20', '2023-09-20', 201901);
INSERT INTO `arrange` VALUES ('2019012023-09-29', '2023-09-29', 201901);
INSERT INTO `arrange` VALUES ('2019012026-01-07', '2026-01-07', 201901);
INSERT INTO `arrange` VALUES ('2019022023-09-15', '2023-09-15', 201902);
INSERT INTO `arrange` VALUES ('2019022023-09-19', '2023-09-19', 201902);
INSERT INTO `arrange` VALUES ('2019022023-09-22', '2023-09-22', 201902);
INSERT INTO `arrange` VALUES ('2019022026-01-05', '2026-01-05', 201902);
INSERT INTO `arrange` VALUES ('2019022026-01-07', '2026-01-07', 201902);
INSERT INTO `arrange` VALUES ('2019032023-09-08', '2023-09-08', 201903);
INSERT INTO `arrange` VALUES ('2019032023-09-12', '2023-09-12', 201903);
INSERT INTO `arrange` VALUES ('2019032023-09-14', '2023-09-14', 201903);
INSERT INTO `arrange` VALUES ('2019032023-09-15', '2023-09-15', 201903);
INSERT INTO `arrange` VALUES ('2019032026-01-07', '2026-01-07', 201903);
INSERT INTO `arrange` VALUES ('2019042023-09-11', '2023-09-11', 201904);
INSERT INTO `arrange` VALUES ('2019052023-09-08', '2023-09-08', 201905);
INSERT INTO `arrange` VALUES ('2019052023-09-15', '2023-09-15', 201905);
INSERT INTO `arrange` VALUES ('2019062023-09-08', '2023-09-08', 201906);
INSERT INTO `arrange` VALUES ('2019072023-09-08', '2023-09-08', 201907);
INSERT INTO `arrange` VALUES ('2019072023-09-15', '2023-09-15', 201907);
INSERT INTO `arrange` VALUES ('2019082023-09-08', '2023-09-08', 201908);
INSERT INTO `arrange` VALUES ('2019092023-09-08', '2023-09-08', 201909);
INSERT INTO `arrange` VALUES ('2019092023-09-11', '2023-09-11', 201909);
INSERT INTO `arrange` VALUES ('2019092023-09-12', '2023-09-12', 201909);
INSERT INTO `arrange` VALUES ('2019092023-09-20', '2023-09-20', 201909);
INSERT INTO `arrange` VALUES ('2019092023-09-26', '2023-09-26', 201909);
INSERT INTO `arrange` VALUES ('2019102023-09-08', '2023-09-08', 201910);
INSERT INTO `arrange` VALUES ('20191042023-09-15', '2023-09-15', 2019104);
INSERT INTO `arrange` VALUES ('20191042023-09-16', '2023-09-16', 2019104);
INSERT INTO `arrange` VALUES ('20191042023-09-18', '2023-09-18', 2019104);
INSERT INTO `arrange` VALUES ('20191122023-09-15', '2023-09-15', 2019112);
INSERT INTO `arrange` VALUES ('20191122023-09-16', '2023-09-16', 2019112);
INSERT INTO `arrange` VALUES ('20191122023-09-18', '2023-09-18', 2019112);
INSERT INTO `arrange` VALUES ('2019232023-09-15', '2023-09-15', 201923);
INSERT INTO `arrange` VALUES ('2019242023-09-15', '2023-09-15', 201924);
INSERT INTO `arrange` VALUES ('2019242026-01-07', '2026-01-07', 201924);
INSERT INTO `arrange` VALUES ('2019322023-09-15', '2023-09-15', 201932);
INSERT INTO `arrange` VALUES ('2019322023-09-16', '2023-09-16', 201932);
INSERT INTO `arrange` VALUES ('2019322023-09-18', '2023-09-18', 201932);
INSERT INTO `arrange` VALUES ('2019322026-01-07', '2026-01-07', 201932);
INSERT INTO `arrange` VALUES ('2019332023-09-15', '2023-09-15', 201933);
INSERT INTO `arrange` VALUES ('2019332023-09-16', '2023-09-16', 201933);
INSERT INTO `arrange` VALUES ('2019332023-09-18', '2023-09-18', 201933);
INSERT INTO `arrange` VALUES ('2019332026-01-07', '2026-01-07', 201933);
INSERT INTO `arrange` VALUES ('2019342023-09-15', '2023-09-15', 201934);
INSERT INTO `arrange` VALUES ('2019342023-09-16', '2023-09-16', 201934);
INSERT INTO `arrange` VALUES ('2019342023-09-18', '2023-09-18', 201934);
INSERT INTO `arrange` VALUES ('2019342026-01-07', '2026-01-07', 201934);
INSERT INTO `arrange` VALUES ('2019352023-09-16', '2023-09-16', 201935);
INSERT INTO `arrange` VALUES ('2019352023-09-18', '2023-09-18', 201935);
INSERT INTO `arrange` VALUES ('2019362023-09-15', '2023-09-15', 201936);
INSERT INTO `arrange` VALUES ('2019362023-09-22', '2023-09-22', 201936);
INSERT INTO `arrange` VALUES ('2019362026-01-07', '2026-01-07', 201936);
INSERT INTO `arrange` VALUES ('2019372023-09-19', '2023-09-19', 201937);
INSERT INTO `arrange` VALUES ('2019372023-09-22', '2023-09-22', 201937);
INSERT INTO `arrange` VALUES ('2019372026-01-07', '2026-01-07', 201937);
INSERT INTO `arrange` VALUES ('2019382023-09-19', '2023-09-19', 201938);
INSERT INTO `arrange` VALUES ('2019392023-09-19', '2023-09-19', 201939);
INSERT INTO `arrange` VALUES ('2019402023-09-19', '2023-09-19', 201940);
INSERT INTO `arrange` VALUES ('2019472023-09-18', '2023-09-18', 201947);
INSERT INTO `arrange` VALUES ('2019472023-09-20', '2023-09-20', 201947);
INSERT INTO `arrange` VALUES ('2019472023-09-22', '2023-09-22', 201947);
INSERT INTO `arrange` VALUES ('2019482023-09-18', '2023-09-18', 201948);
INSERT INTO `arrange` VALUES ('2019482023-09-20', '2023-09-20', 201948);
INSERT INTO `arrange` VALUES ('2019482023-09-22', '2023-09-22', 201948);
INSERT INTO `arrange` VALUES ('2019492023-09-18', '2023-09-18', 201949);
INSERT INTO `arrange` VALUES ('2019492023-09-20', '2023-09-20', 201949);
INSERT INTO `arrange` VALUES ('2019492023-09-22', '2023-09-22', 201949);
INSERT INTO `arrange` VALUES ('2019502023-09-18', '2023-09-18', 201950);
INSERT INTO `arrange` VALUES ('2019502023-09-20', '2023-09-20', 201950);
INSERT INTO `arrange` VALUES ('2019502023-09-22', '2023-09-22', 201950);
INSERT INTO `arrange` VALUES ('2019512023-09-20', '2023-09-20', 201951);
INSERT INTO `arrange` VALUES ('2019512023-09-22', '2023-09-22', 201951);
INSERT INTO `arrange` VALUES ('2019522023-09-01', '2023-09-01', 201952);
INSERT INTO `arrange` VALUES ('2019522023-09-28', '2023-09-28', 201952);
INSERT INTO `arrange` VALUES ('2019522025-12-15', '2025-12-15', 201952);
INSERT INTO `arrange` VALUES ('2019522026-01-07', '2026-01-07', 201952);
INSERT INTO `arrange` VALUES ('2019532023-09-01', '2023-09-01', 201953);
INSERT INTO `arrange` VALUES ('2019542023-09-01', '2023-09-01', 201954);
INSERT INTO `arrange` VALUES ('2019552023-09-01', '2023-09-01', 201955);
INSERT INTO `arrange` VALUES ('2019622023-09-12', '2023-09-12', 201962);
INSERT INTO `arrange` VALUES ('2019822026-01-07', '2026-01-07', 201982);
INSERT INTO `arrange` VALUES ('2019832026-01-07', '2026-01-07', 201983);
INSERT INTO `arrange` VALUES ('2019852023-09-08', '2023-09-08', 201985);
INSERT INTO `arrange` VALUES ('2019852023-09-15', '2023-09-15', 201985);
INSERT INTO `arrange` VALUES ('2019852023-11-22', '2023-11-22', 201985);
INSERT INTO `arrange` VALUES ('2019852024-02-08', '2024-02-08', 201985);
INSERT INTO `arrange` VALUES ('2019852025-12-10', '2025-12-10', 201985);
INSERT INTO `arrange` VALUES ('2019852025-12-28', '2025-12-28', 201985);
INSERT INTO `arrange` VALUES ('2019852025-12-29', '2025-12-29', 201985);
INSERT INTO `arrange` VALUES ('2019852025-12-30', '2025-12-30', 201985);
INSERT INTO `arrange` VALUES ('2019852025-12-31', '2025-12-31', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-02', '2026-01-02', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-03', '2026-01-03', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-04', '2026-01-04', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-05', '2026-01-05', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-06', '2026-01-06', 201985);
INSERT INTO `arrange` VALUES ('2019852026-01-07', '2026-01-07', 201985);
INSERT INTO `arrange` VALUES ('2019902023-09-14', '2023-09-14', 201990);
INSERT INTO `arrange` VALUES ('2020192023-09-08', '2023-09-08', 202019);
INSERT INTO `arrange` VALUES ('2020942023-09-15', '2023-09-15', 202094);
INSERT INTO `arrange` VALUES ('2020942023-09-16', '2023-09-16', 202094);
INSERT INTO `arrange` VALUES ('2020942023-09-18', '2023-09-18', 202094);
INSERT INTO `arrange` VALUES ('2023012023-09-08', '2023-09-08', 202301);
INSERT INTO `arrange` VALUES ('2023012023-09-29', '2023-09-29', 202301);
INSERT INTO `arrange` VALUES ('2023012025-12-30', '2025-12-30', 202301);
INSERT INTO `arrange` VALUES ('2023012026-01-05', '2026-01-05', 202301);
INSERT INTO `arrange` VALUES ('2023012026-01-07', '2026-01-07', 202301);
INSERT INTO `arrange` VALUES ('2025022023-09-15', '2023-09-15', 202502);
INSERT INTO `arrange` VALUES ('2025022023-09-16', '2023-09-16', 202502);
INSERT INTO `arrange` VALUES ('2025022023-09-18', '2023-09-18', 202502);
INSERT INTO `arrange` VALUES ('4567892025-12-30', '2025-12-30', 456789);
INSERT INTO `arrange` VALUES ('4567892026-01-07', '2026-01-07', 456789);

-- ----------------------------
-- Table structure for bed
-- ----------------------------
DROP TABLE IF EXISTS `bed`;
CREATE TABLE `bed`  (
  `b_id` int NOT NULL,
  `p_id` int NULL DEFAULT NULL,
  `b_state` int NULL DEFAULT NULL,
  `b_start` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_id` int NULL DEFAULT NULL,
  `b_reason` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `version` int NULL DEFAULT NULL,
  PRIMARY KEY (`b_id`) USING BTREE,
  INDEX `bTOp`(`p_id` ASC) USING BTREE,
  INDEX `bTOd`(`d_id` ASC) USING BTREE,
  CONSTRAINT `bTOd` FOREIGN KEY (`d_id`) REFERENCES `doctor` (`d_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `bTOp` FOREIGN KEY (`p_id`) REFERENCES `patient` (`p_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bed
-- ----------------------------
INSERT INTO `bed` VALUES (1, 123456, 1, '2023-09-08', 202301, '身体很不好', 2);
INSERT INTO `bed` VALUES (2, 200565, 1, '2025-12-28', 201985, '体质虚弱', 2);
INSERT INTO `bed` VALUES (3, 201801, 1, '2023-09-09', 201909, '发烧', 1);
INSERT INTO `bed` VALUES (4, 201801, 1, '2023-09-11', 201909, '发烧', 1);
INSERT INTO `bed` VALUES (7, 2023401022, 1, '2026-01-06', 201985, '', 3);
INSERT INTO `bed` VALUES (8, -1, 0, NULL, -1, NULL, 1);
INSERT INTO `bed` VALUES (9, -1, 0, NULL, -1, NULL, 1);
INSERT INTO `bed` VALUES (10, 201803, 1, '2023-09-11', 201907, '感冒2', 4);
INSERT INTO `bed` VALUES (11, 201801, 1, '2023-09-11', 201909, '感冒1', 2);
INSERT INTO `bed` VALUES (12, 201801, 1, '2023-09-11', 201909, '发烧1', 2);
INSERT INTO `bed` VALUES (13, 201801, 1, '2023-09-11', 201904, '感冒，需入院观察', 2);
INSERT INTO `bed` VALUES (14, 201801, 1, '2023-09-17', 201901, '感冒', 2);
INSERT INTO `bed` VALUES (15, 201801, 1, '2023-09-14', 201901, '感冒', 2);

-- ----------------------------
-- Table structure for checks
-- ----------------------------
DROP TABLE IF EXISTS `checks`;
CREATE TABLE `checks`  (
  `ch_id` int NOT NULL AUTO_INCREMENT,
  `ch_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `ch_price` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`ch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of checks
-- ----------------------------
INSERT INTO `checks` VALUES (1, 'B超', 201.00);
INSERT INTO `checks` VALUES (2, 'CT', 435.00);
INSERT INTO `checks` VALUES (3, '核磁共振', 653.00);
INSERT INTO `checks` VALUES (4, 'MRI', 534.00);
INSERT INTO `checks` VALUES (5, '心电图', 634.00);
INSERT INTO `checks` VALUES (6, '彩超', 213.00);
INSERT INTO `checks` VALUES (7, '血常规', 434.00);
INSERT INTO `checks` VALUES (8, '肝功能', 543.00);
INSERT INTO `checks` VALUES (9, '血糖', 434.00);
INSERT INTO `checks` VALUES (10, '甲状腺', 434.00);
INSERT INTO `checks` VALUES (11, '视力', 50.00);

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `d_id` int NOT NULL,
  `d_password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_card` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_post` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_introduction` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_section` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `d_state` int NOT NULL,
  `d_price` decimal(10, 2) NULL DEFAULT NULL,
  `d_people` int NULL DEFAULT NULL,
  `d_star` decimal(10, 2) NULL DEFAULT NULL,
  `d_avg_star` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`d_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (-1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO `doctor` VALUES (201900, '3d7dd7b26500bd0595573b651d0080fd', '刘虎', '女', '18211272779', '440881199701203551', '130794144@qq.com', '副主任医师', '很牛逼', '发热门诊', 1, 12.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (201901, '3d7dd7b26500bd0595573b651d0080fd', '张敬轩', '男', '18211272779', '440881199701203551', 'hins555ph@163.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 32.00, 13, 62.00, 4.77);
INSERT INTO `doctor` VALUES (201902, '3d7dd7b26500bd0595573b651d0080fd', '张柏芝', '男', '18211272779', '440881199701203551', '1092839910@qq.com', '主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '消化内科', 1, 12.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (201903, '3d7dd7b26500bd0595573b651d0080fd', '刘德华', '女', '18211272779', '440881199701203551', '1309283910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 32.00, 2, 9.00, 4.50);
INSERT INTO `doctor` VALUES (201904, '3d7dd7b26500bd0595573b651d0080fd', '张学友', '女', '18211272779', '440881199701203551', '1123092839910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 0, 12.00, 2, 10.00, 5.00);
INSERT INTO `doctor` VALUES (201905, '3d7dd7b26500bd0595573b651d0080fd', '郭富城', '男', '18211272779', '440881199701203551', '12313092839910@qq.com', '主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 42.00, 2, 10.00, 5.00);
INSERT INTO `doctor` VALUES (201906, '3d7dd7b26500bd0595573b651d0080fd', '黎明', '女', '18211272779', '440881199701203551', '1233092839910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 5.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201907, '3d7dd7b26500bd0595573b651d0080fd', '谭雪', '女', '18211272779', '440881199701203551', '4313092839910@qq.com', '主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 32.00, 2, 9.00, 4.50);
INSERT INTO `doctor` VALUES (201908, '3d7dd7b26500bd0595573b651d0080fd', '林彪', '男', '18211272779', '440881199701203551', '43213092839910@qq.com', '主治医生', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 43.00, 2, 10.00, 5.00);
INSERT INTO `doctor` VALUES (201909, '3d7dd7b26500bd0595573b651d0080fd', '吴彦祖', '男', '18211272779', '440881199701203551', '64513092839910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '妇科', 1, 54.00, 17, 80.00, 4.71);
INSERT INTO `doctor` VALUES (201910, '3d7dd7b26500bd0595573b651d0080fd', '彭于晏', '男', '18211272779', '440881199701203551', '674313092839910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201911, '3d7dd7b26500bd0595573b651d0080fd', '林峰', '男', '18211272779', '440881199701203551', '7563413092839910@qq.com', '副主任医师', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '妇科', 1, 32.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201912, '3d7dd7b26500bd0595573b651d0080fd', '张杰', '男', '18211272779', '440881199701203551', '745213092839910@qq.com', '主治医生', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '心血管内科', 1, 32.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201913, '3d7dd7b26500bd0595573b651d0080fd', '阿达', '男', '18211272779', '440881199701203551', '2525213092839910@qq.com', '主治医生', '忠实践行中医诊疗术法三十多年，能熟练运用中医理论对常见病、疑难病、急慢性疾病施行救治，尤其对咳、喘、胃病、胸痹等有较深心得，调治经带有较好的效果，诊治未病深有体会。', '妇科', 1, 54.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201921, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '130413434@qq.com', '主治医生', '医术高明', '妇科', 1, 13.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201922, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134349@qq.com', '主治医生', '医术高明', '妇科', 1, 14.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201923, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134348@qq.com', '主治医生', '医术高明', '心血管内科', 1, 15.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201924, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134347@qq.com', '主治医生', '医术高明', '心血管内科', 1, 16.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201925, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134346@qq.com', '主治医生', '医术高明', '妇科', 1, 17.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201926, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134345@qq.com', '副主任医师', '医术高明', '妇科', 1, 18.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201927, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134344@qq.com', '副主任医师', '医术高明', '妇科', 1, 19.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201928, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134343@qq.com', '副主任医师', '医术高明', '妇科', 1, 20.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201929, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134342@qq.com', '副主任医师', '医术高明', '妇科', 1, 21.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201930, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '1304134341@qq.com', '副主任医师', '医术高明', '妇科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201931, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313123@qq.com', '主治医生', '医术高明', '妇科', 1, 13.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201932, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313124@qq.com', '主治医生', '医术高明', '内分泌科', 1, 14.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201933, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313125@qq.com', '主治医生', '医术高明', '内分泌科', 1, 15.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201934, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1321313@qq.com', '主治医生', '医术高明', '内分泌科', 1, 16.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201935, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313127@qq.com', '主治医生', '医术高明', '内分泌科', 1, 17.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201936, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313128@qq.com', '主任医师', '医术高明', '消化内科', 1, 18.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201937, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313129@qq.com', '主任医师', '医术高明', '消化内科', 1, 19.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201938, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313130@qq.com', '主任医师', '医术高明', '消化内科', 1, 20.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201939, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '1313131@qq.com', '主任医师', '医术高明', '消化内科', 1, 21.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201940, '3d7dd7b26500bd0595573b651d0080fd', '蔡晓彤', '男', '18211272779', '550881199701203511', '13131321@qq.com', '主任医师', '医术高明', '消化内科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201941, '3d7dd7b26500bd0595573b651d0080fd', '陈达', '女', '18211272779', '550881199701203511', '13131332@qq.com', '主治医生', '医术高明', '心血管科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201942, '3d7dd7b26500bd0595573b651d0080fd', '陈达', '女', '18211272779', '550881199701203511', '131313422@qq.com', '主治医生', '医术高明', '心血管科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201943, '3d7dd7b26500bd0595573b651d0080fd', '陈达', '女', '18211272779', '550881199701203511', '13131351@qq.com', '主治医生', '医术高明', '心血管科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201944, '3d7dd7b26500bd0595573b651d0080fd', '陈达', '女', '18211272779', '550881199701203511', '13131326@qq.com', '主治医生', '医术高明', '肾内科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201945, '3d7dd7b26500bd0595573b651d0080fd', '李现', '女', '18211272779', '550881199701203511', '13133137@qq.com', '主治医生', '医术高明', '肾内科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201946, '3d7dd7b26500bd0595573b651d0080fd', '李现', '女', '18211272779', '550881199701203511', '131341138@qq.com', '主治医生', '医术高明', '肾内科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201947, '3d7dd7b26500bd0595573b651d0080fd', '李现', '女', '18211272779', '550881199701203511', '1313312139@qq.com', '主治医生', '医术高明', '发热门诊', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201948, '3d7dd7b26500bd0595573b651d0080fd', '周星驰', '女', '18211272779', '550881199701203511', '131311240@qq.com', '主治医生', '医术高明', '发热门诊', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201949, '3d7dd7b26500bd0595573b651d0080fd', '周星驰', '女', '18211272779', '550881199701203511', '1313232141@qq.com', '副主任医师', '医术高明', '发热门诊', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201950, '3d7dd7b26500bd0595573b651d0080fd', '周星驰', '女', '18211272779', '550881199701203511', '1313233142@qq.com', '副主任医师', '医术高明', '发热门诊', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201951, '3d7dd7b26500bd0595573b651d0080fd', '周星驰', '女', '18211272779', '550881199701203511', '1313132343@qq.com', '副主任医师', '医术高明', '发热门诊', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201952, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '1313132344@qq.com', '副主任医师', '医术高明', '手足外科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201953, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '131543145@qq.com', '副主任医师', '医术高明', '手足外科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201954, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '1313453453146@qq.com', '副主任医师', '医术高明', '手足外科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201955, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '1354513147@qq.com', '副主任医师', '医术高明', '手足外科', 1, 23.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201956, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '13153453148@qq.com', '副主任医师', '医术高明', '普通外科', 1, 24.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201957, '3d7dd7b26500bd0595573b651d0080fd', '周线', '女', '18211272779', '550881199701203511', '1313656149@qq.com', '副主任医师', '医术高明', '普通外科', 1, 25.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201958, '3d7dd7b26500bd0595573b651d0080fd', '陈世美', '女', '18211272779', '550881199701203511', '1313132150@qq.com', '副主任医师', '医术高明', '普通外科', 1, 26.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201959, '3d7dd7b26500bd0595573b651d0080fd', '陈世美', '女', '18211272779', '550881199701203511', '13137465151@qq.com', '副主任医师', '医术高明', '肛肠外科', 1, 27.00, 4, 16.00, 4.00);
INSERT INTO `doctor` VALUES (201960, '3d7dd7b26500bd0595573b651d0080fd', '陈世美', '女', '18211272779', '550881199701203511', '143313152@qq.com', '副主任医师', '医术高明', '肛肠外科', 1, 28.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201961, '3d7dd7b26500bd0595573b651d0080fd', '陈世美', '女', '18211272779', '550881199701203511', '1356413153@qq.com', '副主任医师', '医术高明', '肛肠外科', 1, 29.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201962, '3d7dd7b26500bd0595573b651d0080fd', '陈世美', '女', '18211272779', '550881199701203511', '13756413154@qq.com', '副主任医师', '医术高明', '神经外科', 1, 30.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (201963, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '1334534513155@qq.com', '副主任医师', '医术高明', '神经外科', 1, 31.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201964, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '1312313156@qq.com', '副主任医师', '医术高明', '神经外科', 1, 32.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201965, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '13231213157@qq.com', '副主任医师', '医术高明', '泌尿外科', 1, 33.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201966, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '1313534158@qq.com', '副主任医师', '医术高明', '泌尿外科', 1, 34.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201967, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '13136456159@qq.com', '主任医师', '医术高明', '泌尿外科', 1, 35.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201968, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '女', '18211272779', '550881199701203511', '13124313160@qq.com', '主任医师', '医术高明', '骨科', 1, 36.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (201969, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '女', '18211272779', '550881199701203511', '1313867161@qq.com', '主任医师', '医术高明', '骨科', 1, 37.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201970, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '女', '18211272779', '550881199701203511', '1313976678162@qq.com', '主任医师', '医术高明', '骨科', 1, 38.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201971, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '女', '18211272779', '550881199701203511', '13131678163@qq.com', '主任医师', '医术高明', '烧伤整形外科', 1, 39.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201972, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '女', '18211272779', '550881199701203511', '1313821678164@qq.com', '主任医师', '医术高明', '烧伤整形外科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201973, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '131313165@qq.com', '主任医师', '医术高明', '烧伤整形外科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201974, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '131313166@qq.com', '主任医师', '医术高明', '产科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201975, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '131336167@qq.com', '主任医师', '医术高明', '产科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201976, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '1908313168@qq.com', '主任医师', '医术高明', '产科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201977, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '1908313169@qq.com', '主任医师', '医术高明', '儿科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201978, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '10789313170@qq.com', '主任医师', '医术高明', '儿科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201979, '3d7dd7b26500bd0595573b651d0080fd', '钟嘉欣', '男', '18211272779', '550881199701203511', '13089013171@qq.com', '主任医师', '医术高明', '儿科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201980, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@163.com', '副主任医师', '医术高明', '儿童保健科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201981, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@164.com', '副主任医师', '医术高明', '儿童保健科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201982, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', 'dsd113121@165.com', '副主任医师', '医术高明', '耳鼻咽喉科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201983, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '121113121@166.com', '副主任医师', '医术高明', '耳鼻咽喉科', 1, 40.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201984, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@167.com', '副主任医师', '医术高明', '眼科', 1, 12.00, 2, 10.00, 5.00);
INSERT INTO `doctor` VALUES (201985, '3d7dd7b26500bd0595573b651d0080fd', '谢安琪', '女', '18211272779', '550881199701203511', '113121@168.com', '副主任医师', '医术高明', '神经内科', 1, 12.00, 12, 60.00, 5.00);
INSERT INTO `doctor` VALUES (201986, '3d7dd7b26500bd0595573b651d0080fd', '谢安琪', '女', '18211272779', '550881199701203511', '113121@169.com', '副主任医师', '医术高明', '口腔科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201987, '3d7dd7b26500bd0595573b651d0080fd', '谢安琪', '女', '18211272779', '550881199701203511', '113121@170.com', '副主任医师', '医术高明', '口腔科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201988, '3d7dd7b26500bd0595573b651d0080fd', '谢安琪', '女', '18211272779', '550881199701203511', '113121@171.com', '主治医生', '医术高明', '中医科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201989, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@172.com', '主治医生', '医术高明', '中医科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201990, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@173.com', '主治医生', '医术高明', '康复医学科', 1, 12.00, 2, 9.00, 4.50);
INSERT INTO `doctor` VALUES (201991, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@174.com', '主治医生', '医术高明', '康复医学科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201992, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '女', '18211272779', '550881199701203511', '113121@175.com', '主治医生', '医术高明', '急诊科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201993, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '男', '18211272779', '550881199701203511', '113121@176.com', '主治医生', '医术高明', '急诊科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201994, '3d7dd7b26500bd0595573b651d0080fd', '张敬轩', '男', '18211272779', '550881199701203511', '113121@177.com', '主治医生', '医术高明', '急诊科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201995, '3d7dd7b26500bd0595573b651d0080fd', '张敬轩', '男', '18211272779', '550881199701203511', '113121@178.com', '主治医生', '医术高明', '皮肤性病科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201996, '3d7dd7b26500bd0595573b651d0080fd', '张敬轩', '男', '18211272779', '550881199701203511', '113121@179.com', '主治医生', '医术高明', '皮肤性病科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201997, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '男', '18211272779', '550881199701203511', '113121@180.com', '主治医生', '医术高明', '皮肤性病科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201998, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '男', '18211272779', '550881199701203511', '113121@181.com', '主治医生', '医术高明', '功能科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (201999, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '男', '18211272779', '550881199701203511', '113121@181.com', '主治医生', '医术高明', '功能科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202001, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '113121@181.com', '主治医生', NULL, '妇科', 1, 21.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202002, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '11311121@181.com', '主治医生', NULL, '妇科', 1, 32.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202003, '3d7dd7b26500bd0595573b651d0080fd', '陈柏', '女', '18211272779', '550881199701203511', '11323121@181.com', '主治医生', NULL, '妇科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202004, '3d7dd7b26500bd0595573b651d0080fd', '柏宇', '女', '18211272779', '550881199701203511', '23113121@181.com', '主治医生', NULL, '妇科', 1, 23.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202005, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '43113121@181.com', '主治医生', NULL, '妇科', 1, 22.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202006, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '53113121@181.com', '主治医生', NULL, '妇科', 1, 32.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202007, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '65113121@181.com', '主治医生', NULL, '妇科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202008, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '131113121@181.com', '主治医生', NULL, '妇科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202009, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '113654121@181.com', '主治医生', NULL, '妇科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202010, '3d7dd7b26500bd0595573b651d0080fd', '陈柏宇', '女', '18211272779', '550881199701203511', '7511343121@181.com', '主治医生', NULL, '妇科', 1, 54.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202011, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '男', '18211272779', '550881199701203511', '113125435241@181.com', '主治医生', '医术高明', '妇科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202012, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '男', '18211272779', '550881199701203511', '113524524121@181.com', '主治医生', '医术高明', '妇科', 1, 54.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202013, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '男', '18211272779', '550881199701203511', '524524113121@181.com', '主治医生', '医术高明', '妇科', 1, 45.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202014, '3d7dd7b26500bd0595573b651d0080fd', '刘宪华', '男', '18211272779', '550881199701203511', '323113121@181.com', '副主任医师', '医术高明', '妇科', 1, 21.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202015, '3d7dd7b26500bd0595573b651d0080fd', '周深', '男', '18211272779', '550881199701203511', '4343113121@181.com', '副主任医师', '医术高明', '妇科', 1, 43.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202016, '3d7dd7b26500bd0595573b651d0080fd', '周深', '女', '18211272779', '550881199701203511', '5345113121@181.com', '副主任医师', '医术高明', '妇科', 1, 65.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202017, '3d7dd7b26500bd0595573b651d0080fd', '周深', '女', '18211272779', '550881199701203511', 'rwef113121@181.com', '副主任医师', '医术高明', '妇科', 1, 23.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202018, '3d7dd7b26500bd0595573b651d0080fd', '周深', '女', '18211272779', '550881199701203511', '113fadf121@181.com', '副主任医师', '医术高明', '妇科', 1, 54.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202019, '3d7dd7b26500bd0595573b651d0080fd', '周深', '女', '18211272779', '550881199701203511', 'fad113121@181.com', '副主任医师', '医术高明', '妇科', 1, 65.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (202090, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '1313123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202091, 'fa0dbf264c2f14f61e9273fbf2906fc9', '谢杰衡', '男', '18211272779', '550881199701203511', '1313124@qq.com', '主治医生', '医术高明', '儿科', 1, 14.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202092, 'fa0dbf264c2f14f61e9273fbf2906fc9', '陈静娴', '女', '18211272779', '550881199701203511', '1313125@qq.com', '主治医生', '医术高明', '儿科', 1, 15.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202093, 'fa0dbf264c2f14f61e9273fbf2906fc9', '陈三弟', '男', '18211272779', '550881199701203511', '1321313@qq.com', '主治医生', '医术高明', '产科', 1, 16.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202094, 'fa0dbf264c2f14f61e9273fbf2906fc9', '蔡晓彤', '女', '18211272779', '550881199701203511', '1313127@qq.com', '主治医生', '医术高明', '内分泌科', 1, 17.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202095, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '131311123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202096, 'fa0dbf264c2f14f61e9273fbf2906fc9', '谢杰衡', '男', '18211272779', '550881199701203511', '131313224@qq.com', '主治医生', '医术高明', '儿科', 1, 14.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202097, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '138811qa123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202098, 'fa0dbf264c2f14f61e9273fbf2906fc9', '谢杰衡', '男', '18211272779', '550881199701203511', '131313hh224@qq.com', '主治医生', '医术高明', '儿科', 1, 14.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202099, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '138811qa123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (202301, '3d7dd7b26500bd0595573b651d0080fd', '小白白', '男', '15073171170', '550881199701203511', '3289948684@qq.com', '主任医师', '主任医生', '神经内科', 1, 200.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (202502, '3d7dd7b26500bd0595573b651d0080fd', '张三', '男', '15615615611', '123456789012345678', '15615615611@qq.com', '主任医师', '小熊专注提供优质源码，访问地址http://code51.cn', '内分泌科', 1, 10.00, 1, 5.00, 5.00);
INSERT INTO `doctor` VALUES (456789, '3d7dd7b26500bd0595573b651d0080fd', '黄明智', '男', '19151800049', '789456123123456789', '3115836016@qq.com', '主任医师', '他很厉害！！！！！！', '神经内科', 1, 1000.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (2019100, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '1313123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2019101, 'fa0dbf264c2f14f61e9273fbf2906fc9', '谢杰衡', '男', '18211272779', '550881199701203511', '1313124@qq.com', '主治医生', '医术高明', '儿科', 1, 14.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2019102, 'fa0dbf264c2f14f61e9273fbf2906fc9', '陈静娴', '女', '18211272779', '550881199701203511', '1313125@qq.com', '主治医生', '医术高明', '儿科', 1, 15.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2019103, 'fa0dbf264c2f14f61e9273fbf2906fc9', '陈三弟', '男', '18211272779', '550881199701203511', '1321313@qq.com', '主治医生', '医术高明', '产科', 1, 16.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2019104, 'fa0dbf264c2f14f61e9273fbf2906fc9', '蔡晓彤', '女', '18211272779', '550881199701203511', '1313127@qq.com', '主治医生', '医术高明', '内分泌科', 1, 17.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2019112, '3d7dd7b26500bd0595573b651d0080fd', '张大幅', '男', '18231311332', '440891381238123', '3123134124@1213.com', '主任医师', '精通医术', '内分泌科', 1, 12.00, 0, 0.00, NULL);
INSERT INTO `doctor` VALUES (2020991, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '138811qa123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2020992, 'fa0dbf264c2f14f61e9273fbf2906fc9', '张大枪', '男', '18211272779', '550881199701203511', '138811qa123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);
INSERT INTO `doctor` VALUES (2020999, 'fa0dbf264c2f14f61e9273fbf2906fc9', '杨晓彤', '男', '18211272779', '550881199701203511', '138811qa123@qq.com', '主治医生', '医术高明', '产科', 1, 13.00, 0, 0.00, 5.00);

-- ----------------------------
-- Table structure for drug
-- ----------------------------
DROP TABLE IF EXISTS `drug`;
CREATE TABLE `drug`  (
  `dr_id` int NOT NULL AUTO_INCREMENT,
  `dr_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `dr_price` decimal(10, 2) NULL DEFAULT NULL,
  `dr_number` int NULL DEFAULT NULL,
  `dr_publisher` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `dr_unit` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`dr_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of drug
-- ----------------------------
INSERT INTO `drug` VALUES (1, '青霉素', 23.00, 69, '国家医药局', '袋');
INSERT INTO `drug` VALUES (2, '苯唑西林', 11.00, 19, '国家医药局', '盒');
INSERT INTO `drug` VALUES (3, '氨苄西林', 13.00, 48, '国家医药局', '盒');
INSERT INTO `drug` VALUES (4, '哌拉西林', 2.00, 2, '国家医药局', '盒');
INSERT INTO `drug` VALUES (5, '阿莫西林', 13.00, 20, '国家医药局', '盒');
INSERT INTO `drug` VALUES (6, '头孢唑林', 3.00, 32, '国家医药局', '盒');
INSERT INTO `drug` VALUES (7, '头孢氨苄', 4.00, 43, '国家医药局', '盒');
INSERT INTO `drug` VALUES (8, '头孢呋辛', 8.00, 54, '国家医药局', '盒');
INSERT INTO `drug` VALUES (9, '阿米卡星', 5.00, 54, '国家医药局', '袋');
INSERT INTO `drug` VALUES (10, '庆大霉素', 7.00, 64, '国家医药局', '袋');
INSERT INTO `drug` VALUES (11, '红霉素', 6.00, 76, '国家医药局', '袋');
INSERT INTO `drug` VALUES (12, '阿奇霉素', 54.00, 52, '国家医药局', '袋');
INSERT INTO `drug` VALUES (13, '克林霉素', 65.00, 20, '国家医药局', '袋');
INSERT INTO `drug` VALUES (14, '复方磺胺甲噁唑', 76.00, 53, '国家医药局', '袋');
INSERT INTO `drug` VALUES (15, '诺氟沙星', 65.00, 33, '国家医药局', '袋');
INSERT INTO `drug` VALUES (16, '左氧氟沙星', 76.00, 42, '国家医药局', '袋');
INSERT INTO `drug` VALUES (17, '感康', 15.00, 10, '感康集团', '盒');
INSERT INTO `drug` VALUES (18, '感冒药', 21.00, 100, '感冒集团', '袋');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `o_id` int NOT NULL AUTO_INCREMENT,
  `p_id` int NULL DEFAULT NULL,
  `d_id` int NULL DEFAULT NULL,
  `o_record` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `o_start` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `o_end` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `o_state` int NULL DEFAULT NULL,
  `o_drug` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `o_check` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `o_total_price` decimal(10, 2) NULL DEFAULT NULL,
  `o_price_state` int NULL DEFAULT NULL,
  `o_advice` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`o_id`) USING BTREE,
  INDEX `oTOp`(`p_id` ASC) USING BTREE,
  INDEX `0TOd`(`d_id` ASC) USING BTREE,
  CONSTRAINT `0TOd` FOREIGN KEY (`d_id`) REFERENCES `doctor` (`d_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `oTOp` FOREIGN KEY (`p_id`) REFERENCES `patient` (`p_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2023410753 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 201808, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (2, 201801, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (3, 201801, 201938, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (4, 201801, 201938, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (5, 201805, 201938, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (6, 201805, 201922, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (7, 201805, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (8, 201805, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。多喝热水，多运动，定期检查');
INSERT INTO `orders` VALUES (9, 201805, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, NULL, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。多喝热水，多运动，定期检查');
INSERT INTO `orders` VALUES (11, 201805, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (12, 201804, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (13, 201804, 201921, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-07 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (14, 201804, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-08 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (15, 201804, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-08 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (16, 201803, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (17, 201803, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (18, 201803, 201992, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (19, 201803, 201992, '感冒流鼻涕', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (21, 201803, 201992, '感冒流鼻涕', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (22, 201803, 201992, '感冒流鼻涕', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (23, 201803, 201992, '感冒流鼻涕', '2023-09-08 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (24, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (25, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (26, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-10 10:38:3', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (27, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (28, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (29, 201807, 201984, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (30, 201807, 201984, '   title: {\n        text: \'挂号人们性别比例\',\n        left: \'center\'\n    },', '2023-09-09 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (124794, 123456, 201985, '', '2025-12-31 16:30:00', '2025-12-31 11:34:32', 1, '青霉素*23(元)*4  药物总价92元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (124923, 123456, 202301, '身体不好', '2023-09-08 14:30-15:30', '2023-09-08 13:11:54', 1, '青霉素*23(元)*1 氨苄西林*13(元)*1  药物总价36元 ', 'B超*201(元) 肝功能*543(元)  项目总价744元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (128224, 123456, 201985, NULL, '2023-09-08 08:30-09:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (128436, 123456, 201985, '', '2025-12-31 16:30:00', '2025-12-31 11:38:22', 1, ' 药物总价0元 ', 'B超*201(元) B超*201(元) B超*201(元) B超*201(元)  项目总价804元 ', 804.00, 0, NULL);
INSERT INTO `orders` VALUES (132217, 123456, 201985, NULL, '2023-11-22 09:30-10:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (132872, 123456, 201985, NULL, '2024-02-08 10:30-11:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (194551, 191744, 201985, '', '2025-12-30 10:30:00.00', '2025-12-30 11:28:14', 1, '苯唑西林*11(元)*1  药物总价11元 ', ' 项目总价0元 ', 11.00, 0, NULL);
INSERT INTO `orders` VALUES (200977, 191744, 201985, '', '2025-12-30 14:30:00.00', '2025-12-30 11:22:51', 1, '青霉素*23(元)*2  药物总价46元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (201728, 191744, 201985, NULL, '2025-12-30 16:30:00', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (201856, 200565, 201985, NULL, '2025-12-29 08:30:00.00', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (202810, 201801, 201984, '发烧，体温38摄氏度', '2023-09-09 08:30-09:30', '2023-09-17 10:46:55', 1, '苯唑西林*11(元)*1 青霉素*23(元)*2  药物总价57元 氨苄西林*13(元)*1 药物总价13元', 'CT*435(元)  项目总价435元 项目总价0元', 0.00, 1, '报告显示无大碍，建议多喝热水');
INSERT INTO `orders` VALUES (202967, 201821, 201968, '感冒', '2023-09-12 14:30-15:30', '2023-09-12 15:40:11', 1, '青霉素*23(元)*3 苯唑西林*11(元)*1  药物总价80元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (203010, 201801, 201984, NULL, '2023-09-09 08:30-09:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (203029, 201802, 201984, '感冒', '2023-09-09 08:30-09:30', '2023-09-26 22:44:37', 1, '哌拉西林*2(元)*1  药物总价2元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (203100, 201801, 201992, '感冒', '2023-09-09 08:30-09:30', '2023-09-26 22:44:24', 1, '青霉素*23(元)*2  药物总价46元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (203248, 201803, 201992, '感冒流鼻涕', '2023-09-09 08:30-09:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (203271, 200565, 201985, NULL, '2025-12-29 08:30:00.00', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (203286, 201801, 201901, NULL, '2023-09-15 14:30-15:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (203397, 201803, 201959, '感冒，呕吐，疑似食物中毒', '2023-09-10 08:30-09:30', '2023-09-17 10:52:12', 1, '氨苄西林*13(元)*1 苯唑西林*11(元)*2  药物总价35元 哌拉西林*2(元)*1 药物总价2元', 'B超*201(元)  项目总价201元 项目总价0元', 0.00, 1, '报告显示无大碍');
INSERT INTO `orders` VALUES (203399, 201821, 201968, '感冒发烧', '2023-09-12 08:30-09:30', '2023-09-12 15:50:14', 1, '哌拉西林*2(元)*2  药物总价4元 ', '核磁共振*653(元)  项目总价653元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (203460, 200565, 201985, '', '2025-12-28 16:30:00.00', '2025-12-28 9:44:21', 1, '青霉素*23(元)*1 苯唑西林*11(元)*1  药物总价34元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (203604, 201801, 201959, '感冒', '2023-09-10 08:30-09:30', '2023-09-09 15:37:30', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (203688, 201801, 201959, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-10 08:30-09:30', '2023-09-11 15:5:52', 1, ' 药物总价0元 青霉素*23(元)*2 苯唑西林*11(元)*2 氨苄西林*13(元)*1 药物总价81元', 'CT*435(元) 核磁共振*653(元)  项目总价1088元 项目总价0元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。多喝热水，多运动，定期检查');
INSERT INTO `orders` VALUES (203762, 201801, 201984, '感冒', '2023-09-10 08:30-09:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (203838, 201802, 201959, '感冒', '2023-09-10 08:30-09:30', '2023-09-26 22:43:51', 1, '哌拉西林*2(元)*1 氨苄西林*13(元)*2  药物总价28元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (204315, 201804, 201959, '感冒流鼻涕', '2023-09-10 08:30-09:30', '2023-09-10 18:2:28', 1, ' 药物总价0元 ', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (204574, 200565, 201985, '', '2025-12-30 14:30:00.00', '2025-12-30 11:16:21', 1, '苯唑西林*11(元)*2  药物总价22元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (204644, 201801, 201959, '感冒', '2023-09-10 08:30-09:30', '2023-09-26 22:44:0', 1, '哌拉西林*2(元)*2 氨苄西林*13(元)*1  药物总价17元 ', 'CT*435(元)  项目总价435元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (204715, 201801, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-15 14:30-15:30', '2023-09-15 12:21:14', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 苯唑西林*11(元)*1 青霉素*23(元)*2 药物总价57元苯唑西林*11(元)*2 青霉素*23(元)*1 药物总价45元', 'B超*201(元)  项目总价201元 项目总价0元项目总价0元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。建议多喝热水，多运动，定期检查。');
INSERT INTO `orders` VALUES (204983, 201811, 201968, '感冒发烧', '2023-09-12 10:30-11:30', '2023-09-12 15:47:6', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (204996, 201801, 201984, '感冒', '2023-09-10 08:30-09:30', '2023-09-26 22:44:42', 1, '哌拉西林*2(元)*1  药物总价2元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (205209, 201802, 201959, '感冒流鼻涕', '2023-09-10 08:30-09:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (205213, 201801, 201959, '感冒', '2023-09-10 08:30-09:30', '2023-09-26 22:44:53', 1, '氨苄西林*13(元)*1  药物总价13元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (205267, 201802, 201984, '感冒', '2023-09-10 08:30-09:30', '2023-09-26 22:45:0', 1, '哌拉西林*2(元)*1  药物总价2元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (205483, 200565, 201985, NULL, '2025-12-29 08:30-09:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (205601, 201801, 201959, NULL, '2023-09-10 08:30-09:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (206109, 201801, 201968, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:43:43', 1, '氨苄西林*13(元)*1  药物总价13元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (206146, 201801, 201968, '感冒发烧，轻微鼻炎', '2023-09-11 08:30-09:30', '2023-09-10 8:37:14', 1, '苯唑西林*11(元)*1 青霉素*23(元)*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (206434, 201801, 201959, '感冒流鼻涕', '2023-09-11 08:30-09:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (206526, 200565, 201985, NULL, '2025-12-29 08:30-09:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (206611, 201801, 201900, NULL, '2023-09-13 14:30-15:30', '2023-09-09 15:37:30', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (206735, 201802, 201959, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:43:35', 1, '青霉素*23(元)*1 苯唑西林*11(元)*1  药物总价34元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (206814, 201801, 201959, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:44:48', 1, '哌拉西林*2(元)*1  药物总价2元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (206843, 201802, 201959, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:44:30', 1, '氨苄西林*13(元)*1  药物总价13元 ', 'CT*435(元)  项目总价435元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (206971, 201801, 201959, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:42:59', 1, '哌拉西林*2(元)*3  药物总价6元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (207186, 201803, 201968, '前列腺肿大，初步诊断过敏', '2023-09-11 08:30-09:30', '2023-09-10 10:38:3', 1, '氨苄西林*13(元)*1 苯唑西林*11(元)*1  药物总价24元 苯唑西林*11(元)*1 青霉素*23(元)*1 药物总价34元', 'CT*435(元)  项目总价435元 项目总价0元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (207476, 201802, 201959, '感冒', '2023-09-11 08:30-09:30', '2023-09-26 22:44:17', 1, '苯唑西林*11(元)*2  药物总价22元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (207752, 201802, 201900, '感冒', '2023-09-13 14:30-15:30', '2023-09-26 22:43:24', 1, '青霉素*23(元)*2  药物总价46元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (207917, 201801, 201900, '感冒流鼻涕', '2023-09-13 14:30-15:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (208012, 201811, 201990, '发烧流鼻涕', '2023-09-14 08:30-09:30', '2023-09-14 15:17:41', 1, '青霉素*23(元)*3 苯唑西林*11(元)*1  药物总价80元 苯唑西林*11(元)*2 药物总价22元', 'B超*201(元) CT*435(元)  项目总价636元 项目总价0元', 0.00, 1, '报告显示身体无大碍，建议多喝热水');
INSERT INTO `orders` VALUES (208080, 201801, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-15 10:30-11:30', '2023-09-15 11:18:4', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 青霉素*23(元)*2 苯唑西林*11(元)*1 药物总价57元', 'B超*201(元)  项目总价201元 项目总价0元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。建议多喝热水，多运动，定期检查。');
INSERT INTO `orders` VALUES (208218, 201801, 201968, '感冒', '2023-09-12 14:30-15:30', '2023-09-26 22:42:49', 1, '氨苄西林*13(元)*1 哌拉西林*2(元)*1  药物总价15元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (208231, 200565, 201985, '', '2025-12-31 16:30:00', '2025-12-31 17:29:29', 1, '青霉素*23(元)*1  药物总价23元 ', ' 项目总价0元 ', 23.00, 0, NULL);
INSERT INTO `orders` VALUES (208557, 201801, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-15 08:30-09:30', '2023-09-15 13:4:59', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (208674, 202501, 202502, '', '2023-09-15 08:30-09:30', '2023-09-15 10:33:32', 1, '哌拉西林*2(元)*1 氨苄西林*13(元)*1 苯唑西林*11(元)*1 青霉素*23(元)*1  药物总价49元 药物总价0元药物总价0元', 'B超*201(元) CT*435(元) 核磁共振*653(元) MRI*534(元)  项目总价1823元 项目总价0元项目总价0元', 0.00, 1, '小熊专供.小熊专注提供优质源码，访问地址http://code51.cn');
INSERT INTO `orders` VALUES (208774, 201801, 201968, '感冒', '2023-09-12 14:30-15:30', '2023-09-26 22:43:10', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 ', 'CT*435(元)  项目总价435元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (208790, 201802, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-15 09:30-10:30', '2023-09-15 11:9:14', 1, '青霉素*23(元)*2  药物总价46元 ', 'B超*201(元)  项目总价201元 ', 247.00, 0, NULL);
INSERT INTO `orders` VALUES (209007, 201801, 201900, '发烧', '2023-09-13 14:30-15:30', '2023-09-09 15:37:52', 1, '哌拉西林*2*1 氨苄西林*13*1 药物总价15元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (209319, 201801, 201901, NULL, '2023-09-20 08:30-09:30', NULL, 0, NULL, NULL, NULL, 0, NULL);
INSERT INTO `orders` VALUES (209782, 201801, 201900, '发烧', '2023-09-13 14:30-15:30', '2023-09-17 15:58:36', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 ', 'B超*201(元) CT*435(元)  项目总价636元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (209887, 201801, 201900, '感冒', '2023-09-13 14:30-15:30', '2023-09-06 8:45:52', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'B超*201 CT*435 项目总价636元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (209918, 201801, 201901, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-14 10:30-11:30', '2023-09-14 23:24:46', 1, '青霉素*23(元)*2 苯唑西林*11(元)*1  药物总价57元 青霉素*23(元)*2 苯唑西林*11(元)*1 药物总价57元', 'B超*201(元) CT*435(元)  项目总价636元 项目总价0元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。');
INSERT INTO `orders` VALUES (210177, 201803, 201900, '经常出现容易出汗，脾气暴躁，精神不振，乏力，心慌，手抖，脖子突出，眼睛突出等症状', '2023-09-13 14:30-15:30', '2023-09-10 17:3:56', 1, ' 药物总价0元 青霉素*23(元)*2 苯唑西林*11(元)*2 氨苄西林*13(元)*1 药物总价81元', 'CT*435(元) 项目总价435元', 0.00, 1, '两侧胸廓对称。气管居中。双肺门不大，位置及密度正常。双肺纹理分布、走行自然，未见增强、紊乱。双肺野清晰，肺实质内未见异常密度阴影。纵隔未见增宽、移位。心脏形态、大小正常。双膈位置、形态正常，膈面光整，双肋膈角锐利。胸膜无增厚及粘连，胸膜腔未见积液、积气。胸壁骨骼及软组织未见异常。初步确诊为甲状腺癌。医生意见,多喝热水，多运动，定期检查。');
INSERT INTO `orders` VALUES (210270, 200565, 201985, '', '2025-12-28 09:30-10:30', '2025-12-28 15:23:16', 1, '苯唑西林*11(元)*1 左氧氟沙星*76(元)*1  药物总价87元 ', 'CT*435(元) 视力*50(元)  项目总价485元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (210427, 201801, 201900, '感冒流鼻涕', '2023-09-13 14:30-15:30', '2023-09-05 20:30:21', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'B超*201 CT*435 项目总价20436元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (210429, 201801, 201968, '感冒流鼻涕', '2023-09-13 14:30-15:30', '2023-09-10 11:23:23', 1, '青霉素*23*1 苯唑西林*11*1 药物总价34元', 'CT*435(元) 项目总价435元', 0.00, 1, '报告显示并无大碍，建议多喝水');
INSERT INTO `orders` VALUES (211209, 201802, 201968, '感冒', '2023-09-12 14:30-15:30', '2023-09-26 22:42:40', 1, '青霉素*23(元)*1 苯唑西林*11(元)*1  药物总价34元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023402127, 2023401022, 201902, '', '2026-01-05 10:30:00', '2026-01-05 10:34:54', 1, '青霉素*23(元)*1  药物总价23元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023402516, 2023401022, 201902, '', '2026-01-05 14:30:00', '2026-01-05 11:10:14', 1, '青霉素*23(元)*1  药物总价23元  // 青霉素*23(元)*1 药物总价23元 // 青霉素*23(元)*2 药物总价46元', ' 项目总价0元  // CT*435(元) 项目总价435元 // 项目总价0元', 0.00, 1, '');
INSERT INTO `orders` VALUES (2023402524, 2023401022, 201985, '', '2025-12-31 08:30:00', '2025-12-31 11:4:37', 1, '青霉素*23(元)*2  药物总价46元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023402526, 2023401022, 201985, '', '2026-01-04 16:30:00', '2026-01-04 16:9:35', 1, ' 药物总价0元  // 青霉素*23(元)*1 苯唑西林*11(元)*1 氨苄西林*13(元)*1 哌拉西林*2(元)*1 药物总价49元', '彩超*213(元)  项目总价213元  // 项目总价0元', 0.00, 1, '');
INSERT INTO `orders` VALUES (2023402577, 2023401022, 201985, '', '2025-12-31 16:30:00', '2025-12-31 16:39:8', 1, ' 药物总价0元 ', 'MRI*534(元)  项目总价534元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023402681, 2023401022, 201985, '', '2026-01-02 16:30:00', '2026-01-02 17:4:24', 1, ' 药物总价0元 ', 'MRI*534(元) MRI*534(元)  项目总价1068元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023402771, 2023401022, 201985, '', '2025-12-31 16:30:00', '2025-12-31 16:29:16', 1, '青霉素*23(元)*3 氨苄西林*13(元)*1  药物总价82元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023403620, 2023401022, 201985, '', '2026-01-06 10:30:00', '2026-01-06 11:15:7', 1, ' 药物总价0元 ', 'B超*201(元)  项目总价201元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023405328, 2023401022, 201985, '', '2026-01-06 14:30:00', '2026-01-06 11:6:4', 1, '青霉素*23(元)*2  药物总价46元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023405778, 2023401022, 201985, '', '2026-01-06 15:30:00', '2026-01-06 11:48:7', 1, '青霉素*23(元)*2  药物总价46元 ', ' 项目总价0元 ', 46.00, 1, NULL);
INSERT INTO `orders` VALUES (2023405991, 2023401022, 201985, '患者难受\n', '2026-01-06 15:30:00', '2026-01-06 16:9:37', 1, '青霉素*23(元)*1 苯唑西林*11(元)*1  药物总价34元 ', '核磁共振*653(元)  项目总价653元 ', 687.00, 1, NULL);
INSERT INTO `orders` VALUES (2023406058, 2023401022, 201985, '患者吃坏东西，引起肠胃感染', '2026-01-07 09:30:00', '2026-01-07 9:36:6', 1, '青霉素*23(元)*1  药物总价23元 ', 'B超*201(元)  项目总价201元 ', 224.00, 1, NULL);
INSERT INTO `orders` VALUES (2023407032, 2023401022, 201985, '患者因为吃坏东西，胃部感染', '2026-01-07 08:30:00', '2026-01-07 9:25:21', 1, '青霉素*23(元)*1  药物总价23元 ', 'B超*201(元)  项目总价201元 ', 224.00, 1, NULL);
INSERT INTO `orders` VALUES (2023408097, 2023401022, 201985, '感染', '2026-01-06 16:30:00', '2026-01-06 16:49:15', 1, '青霉素*23(元)*1  药物总价23元 ', 'CT*435(元)  项目总价435元 ', 458.00, 1, NULL);
INSERT INTO `orders` VALUES (2023408421, 2023401022, 201985, '', '2026-01-04 15:30:00', '2026-01-04 15:34:13', 1, ' 药物总价0元  // 青霉素*23(元)*1 药物总价23元', '视力*50(元)  项目总价50元  // 项目总价0元', 0.00, 1, '');
INSERT INTO `orders` VALUES (2023409215, 2023401022, 201985, '', '2026-01-06 10:30:00', '2026-01-06 11:15:13', 1, '苯唑西林*11(元)*2  药物总价22元 ', ' 项目总价0元 ', 0.00, 1, NULL);
INSERT INTO `orders` VALUES (2023409674, 2023401022, 201985, '', '2026-01-06 16:30:00', '2026-01-06 15:51:42', 1, ' 药物总价0元 ', '核磁共振*653(元)  项目总价653元 ', 653.00, 1, NULL);

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `p_id` int NOT NULL,
  `p_password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_gender` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_phone` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_card` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_state` int NULL DEFAULT NULL,
  `p_birthday` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `p_age` int NULL DEFAULT NULL,
  `p_phone_verified` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '已验证手机号',
  `p_info_complete` tinyint(1) NULL DEFAULT 0 COMMENT '信息完善状态 0-未完善 1-已完善',
  PRIMARY KEY (`p_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (-1, NULL, NULL, NULL, NULL, NULL, NULL, 1, '1997-01-20', 1, NULL, 0);
INSERT INTO `patient` VALUES (23456, 'e8ad3332e4ac01c14293b2a3289c2f58', '黄明智1', '男', '13695648844', '789456123232165498', '2415965945@qq.com', 1, '2003-12-18', 22, NULL, 0);
INSERT INTO `patient` VALUES (123456, '3d7dd7b26500bd0595573b651d0080fd', 'zs', '男', '15073171170', '550881199701203511', '3289948684@qq.com', 1, '2012-03-06', 11, NULL, 0);
INSERT INTO `patient` VALUES (191744, '61056ed929c668813e3844893e368001', '小孙', '男', '19174439716', '789456123461237', '3466968924@qq.com', 0, '2005-11-30', 20, NULL, 0);
INSERT INTO `patient` VALUES (200565, '61056ed929c668813e3844893e368001', '刘书羽1', '男', '19174439716', '621126200509204456', '3466962014@qq.com', 0, '2003-12-18', 22, NULL, 0);
INSERT INTO `patient` VALUES (201801, '3d7dd7b26500bd0595573b651d0080fd', '小熊', '男', '18211272779', '440881199701203551', 'hins555ph@163.com', 0, '1997-01-20', 2, NULL, 0);
INSERT INTO `patient` VALUES (201802, '3d7dd7b26500bd0595573b651d0080fd', '刘大头', '女', '11213917127', '440881199701203551', '1306931161@qq.com', 1, '1997-01-20', 10, NULL, 0);
INSERT INTO `patient` VALUES (201803, '3d7dd7b26500bd0595573b651d0080fd', '刘倩薇', '女', '18211272779', '440881199701203551', '1306931162@qq.com', 1, '1997-01-20', 12, NULL, 0);
INSERT INTO `patient` VALUES (201804, '3d7dd7b26500bd0595573b651d0080fd', '刘晓庆', '女', '18211272779', '440881199701203551', '1306931163@qq.com', 1, '1997-01-20', 21, NULL, 0);
INSERT INTO `patient` VALUES (201805, '3d7dd7b26500bd0595573b651d0080fd', '刘海', '女', '18211272779', '440881199701203551', '1306931164@qq.com', 1, '1997-01-20', 22, NULL, 0);
INSERT INTO `patient` VALUES (201806, '3d7dd7b26500bd0595573b651d0080fd', '张粗', '女', '18211272779', '440881199701203551', '306931167@qq.com', 1, '1997-01-20', 32, NULL, 0);
INSERT INTO `patient` VALUES (201807, '3d7dd7b26500bd0595573b651d0080fd', '张艳', '男', '18211272779', '440881199701203551', '130693167@qq.com', 0, '1997-01-20', 43, NULL, 0);
INSERT INTO `patient` VALUES (201808, '3d7dd7b26500bd0595573b651d0080fd', '周杰伦', '男', '18211272779', '440881199701203551', '306931167@qq.com', 1, '1997-01-20', 31, NULL, 0);
INSERT INTO `patient` VALUES (201809, '3d7dd7b26500bd0595573b651d0080fd', '周洁', '男', '18211272779', '440881199701203551', '130693167@qq.com', 0, '1997-01-20', 54, NULL, 0);
INSERT INTO `patient` VALUES (201810, '3d7dd7b26500bd0595573b651d0080fd', '周鹏', '男', '18211272779', '440881199701203551', '06931167@qq.com', 1, '1997-01-20', 55, NULL, 0);
INSERT INTO `patient` VALUES (201811, '3d7dd7b26500bd0595573b651d0080fd', '王亚军', '男', '18211272779', '440881199701203551', '130693116@qq.com', 1, '1997-01-20', 66, NULL, 0);
INSERT INTO `patient` VALUES (201812, '3d7dd7b26500bd0595573b651d0080fd', '邓学', '男', '18211272779', '440881199701203551', '1306931167@qq.com', 1, '1997-01-20', 77, NULL, 0);
INSERT INTO `patient` VALUES (201813, '3d7dd7b26500bd0595573b651d0080fd', '赵四', '女', '18323131231', '312313123123123', 'hins55@163.com', 1, '1997-01-20', 24, NULL, 0);
INSERT INTO `patient` VALUES (201819, '3d7dd7b26500bd0595573b651d0080fd', '陈小鹏', '男', '18231214134', '440881192183193102', '12039120@qq.com', 1, '1997-01-19', 88, NULL, 0);
INSERT INTO `patient` VALUES (201820, '3d7dd7b26500bd0595573b651d0080fd', '陈大大', '男', '18127718312', '123123123123123', '131121@qq.com', 1, '1999-03-14', 99, NULL, 0);
INSERT INTO `patient` VALUES (201821, '3d7dd7b26500bd0595573b651d0080fd', '刘晓庆', '女', '18273172121', '123123123123112', '12312312@qq.com', 1, '1998-02-18', 76, NULL, 0);
INSERT INTO `patient` VALUES (202501, '0233515d88b1ffd5da10ea673f498096', '小熊病人', '男', '15615615611', '123456789012345678', '15615615611@qq.com', 1, '2020-10-08', 3, NULL, 0);
INSERT INTO `patient` VALUES (201812345, 'b6add5a4e3ce78f478e76a0cdb330dc6', '黄俊才', '男', '13713182332', '121231241243413443', '123ascqwe@123.com', 0, '2023-09-17', 0, NULL, 0);
INSERT INTO `patient` VALUES (2022400958, '61056ed929c668813e3844893e368001', '黄明智', '女', '19151800049', '621159588585654521', '3115836016@qq.com', 1, '2003-01-08', 23, NULL, 0);
INSERT INTO `patient` VALUES (2023401022, '798203d7a5fecbb517fdb478f396ad0c', 'sw', '男', '13993280037', '123456789123456789', '2415931942@qq.com', 1, '2002-12-11', 23, NULL, 0);

-- ----------------------------
-- Table structure for sms_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `sms_verification_code`;
CREATE TABLE `sms_verification_code`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '手机号',
  `code` varchar(6) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '验证码',
  `type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类型：login/register/etc',
  `expire_time` bigint NOT NULL COMMENT '过期时间戳',
  `used` tinyint NULL DEFAULT 0 COMMENT '是否已使用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_verification_code
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
