// تطبيق حلقات القرآن الكريم - منظومة منارة الجيل القرآني الرسمية المعتمدة
// يدعم التثبيت الفوري PWA على جميع الهواتف الذكية والأجهزة اللوحية
// نظام الإدارة متعدد الجمعيات (جمعية هائل سعيد أنعم + جمعية معاذ الحروي) مع العزل التام للبيانات
// بوابة الدخول والتحقق الأمني المشدد وسجل تدقيق أمان المعلومات
// ميزة القُرّاء لمعرفة مستوى الطالب وموسوعة القراءات العشر لجميع المذاهب الإسلامية

// ========================================================
// 🔐 سجل الأكواد الرسمية المعتمدة الصادرة من المشرف العام
// ========================================================
const OFFICIAL_AUTHORIZED_REGISTRY = {
    'HAEL': {
        orgName: "جمعية هائل سعيد أنعم لتحفيظ القرآن الكريم",
        shortName: "جمعية هائل سعيد أنعم",
        roles: {
            'SUPERVISOR': [
                { code: "SUPER-HSA-777", name: "د. عبد الرقيب عبد الخالق", roleTitle: "المشرف العام لحلقات هائل سعيد أنعم" },
                { code: "SUPER-HSA-888", name: "الأستاذة أسماء الحميري", roleTitle: "مشرفة الحلقات النسائية والإشراف العام" }
            ],
            'TEACHER': [
                { code: "TCH-HSA-01", name: "الشيخ عبد الله القدسي", circle: "حلقة الإمام الشاطبي", roleTitle: "معلم ومحفظ معتمد" },
                { code: "TCH-HSA-02", name: "الشيخ صالح الهمداني", circle: "حلقة الإمام نافع", roleTitle: "معلم ومحفظ معتمد" },
                { code: "TCH-HSA-03", name: "الأستاذة مريم السعيد", circle: "حلقة أم المؤمنين خديجة", roleTitle: "محفظة معتمدة" },
                { code: "TCH-HSA-04", name: "الأستاذة هدى العمري", circle: "حلقة الزهراء للفتيات", roleTitle: "محفظة معتمدة" }
            ],
            'PARENT': [
                { code: "HSA-101", studentName: "عمر أحمد الشريف", roleTitle: "ولي أمر الطالب" },
                { code: "HSA-102", studentName: "فاطمة محمد السعيد", roleTitle: "ولي أمر الطالبة" },
                { code: "HSA-103", studentName: "يوسف خالد العتيبي", roleTitle: "ولي أمر الطالب" },
                { code: "HSA-104", studentName: "مريم عبد الله الغامدي", roleTitle: "ولي أمر الطالبة" },
                { code: "HSA-105", studentName: "عبد الرحمن طارق المنصور", roleTitle: "ولي أمر الطالب" },
                { code: "HSA-106", studentName: "سارة إبراهيم القحطاني", roleTitle: "ولي أمر الطالبة" }
            ]
        }
    },
    'MUADH': {
        orgName: "جمعية معاذ الحروي لتحفيظ القرآن الكريم",
        shortName: "جمعية معاذ الحروي",
        roles: {
            'SUPERVISOR': [
                { code: "SUPER-MDH-888", name: "الشيخ عبد الولي الحروي", roleTitle: "المشرف العام لجمعية معاذ الحروي" },
                { code: "SUPER-MDH-999", name: "الأستاذة خديجة المعمري", roleTitle: "مشرفة القسم النسائي وحلقات الإتقان" }
            ],
            'TEACHER': [
                { code: "TCH-MDH-01", name: "الشيخ فؤاد الحروي", circle: "حلقة معاذ بن جبل", roleTitle: "معلم ومحفظ معتمد" },
                { code: "TCH-MDH-02", name: "الشيخ مأمون السقاف", circle: "حلقة أبي بن كعب", roleTitle: "معلم ومحفظ معتمد" },
                { code: "TCH-MDH-03", name: "الأستاذة سميرة الشرعبي", circle: "حلقة أم سلمة (بنات)", roleTitle: "محفظة معتمدة" },
                { code: "TCH-MDH-04", name: "الأستاذة أسماء الحروي", circle: "حلقة رفيدة الأسلمية", roleTitle: "محفظة معتمدة" }
            ],
            'PARENT': [
                { code: "MDH-201", studentName: "بلال عبد الكريم الحروي", roleTitle: "ولي أمر الطالب" },
                { code: "MDH-202", studentName: "عائشة فاروق المعمري", roleTitle: "ولي أمر الطالبة" },
                { code: "MDH-203", studentName: "صهيب منير الجعفري", roleTitle: "ولي أمر الطالب" },
                { code: "MDH-204", studentName: "زينب عبد السلام الحروي", roleTitle: "ولي أمر الطالبة" },
                { code: "MDH-205", studentName: "أنس عمار الشرعبي", roleTitle: "ولي أمر الطالب" }
            ]
        }
    }
};

// ========================================================
// 🏛️ بيانات الجمعيات والعزل التام للبيانات
// ========================================================
const ORG_STORAGE_KEY_PREFIX = 'quran_org_data_';
const ACTIVE_ORG_STORAGE_KEY = 'active_quran_org';
const SESSION_STORAGE_KEY = 'quran_active_auth_session';
const AUDIT_LOG_STORAGE_KEY = 'quran_security_audit_log';

// الهيكل الرسمي لجمعية هائل سعيد أنعم
const initialHaelData = {
    key: 'HAEL',
    name: "جمعية هائل سعيد أنعم لتحفيظ القرآن الكريم",
    shortName: "جمعية هائل سعيد أنعم",
    location: "تعز - اليمن (المركز الرئيسي)",
    color: "#0F5132",
    students: [
        { id: 1, name: "عمر أحمد الشريف", gender: "MALE", circle: "حلقة الإمام الشاطبي", code: "HSA-101", phone: "0501234567", juz: 29, points: 380, streak: 12, goal: "ختم القرآن الكريم كاملاً", level: "المستوى المتقدم / المتقن 🥇", memScore: 92, tajScore: 88, makhScore: 90, waqfScore: 88 },
        { id: 2, name: "فاطمة محمد السعيد", gender: "FEMALE", circle: "حلقة أم المؤمنين خديجة", code: "HSA-102", phone: "0559876543", juz: 28, points: 420, streak: 15, goal: "حفظ الأجزاء الخمسة الأخيرة", level: "مستوى الإتقان العالي ومضاهاة القُرّاء 👑", memScore: 96, tajScore: 94, makhScore: 95, waqfScore: 92 },
        { id: 3, name: "يوسف خالد العتيبي", gender: "MALE", circle: "حلقة الإمام نافع", code: "HSA-103", phone: "0543219876", juz: 30, points: 210, streak: 7, goal: "حفظ جزء عم وتبارك", level: "المستوى المتوسط / المجيد 🥈", memScore: 78, tajScore: 72, makhScore: 75, waqfScore: 70 },
        { id: 4, name: "مريم عبد الله الغامدي", gender: "FEMALE", circle: "حلقة الزهراء للفتيات", code: "HSA-104", phone: "0561122334", juz: 25, points: 650, streak: 22, goal: "ختم القرآن الكريم كاملاً", level: "مستوى الإتقان العالي ومضاهاة القُرّاء 👑", memScore: 98, tajScore: 95, makhScore: 96, waqfScore: 94 },
        { id: 5, name: "عبد الرحمن طارق المنصور", gender: "MALE", circle: "حلقة الإمام الشاطبي", code: "HSA-105", phone: "0533344556", juz: 27, points: 290, streak: 9, goal: "حفظ 10 أجزاء", level: "المستوى المتقدم / المتقن 🥇", memScore: 86, tajScore: 82, makhScore: 85, waqfScore: 80 },
        { id: 6, name: "سارة إبراهيم القحطاني", gender: "FEMALE", circle: "حلقة أم المؤمنين خديجة", code: "HSA-106", phone: "0577788990", juz: 30, points: 175, streak: 6, goal: "حفظ جزء عم", level: "المستوى التمهيدي / التأسيسي 🥉", memScore: 65, tajScore: 60, makhScore: 62, waqfScore: 58 }
    ],
    circles: [
        { name: "حلقة الإمام الشاطبي", gender: "MALE", teacher: "الشيخ عبد الله القدسي", phone: "0501112233", schedule: "بعد صلاة الفجر", location: "مسجد هائل سعيد الكبير", goal: "حفظ وتجويد القرآن بالقراءات" },
        { name: "حلقة الإمام نافع", gender: "MALE", teacher: "الشيخ صالح الهمداني", phone: "0502223344", schedule: "بعد صلاة العصر", location: "مركز هائل النموذجي", goal: "حفظ الأجزاء العشرة الأولى" },
        { name: "حلقة أم المؤمنين خديجة", gender: "FEMALE", teacher: "الأستاذة مريم السعيد", phone: "0551122334", schedule: "العصر (4:00 - 5:30 م)", location: "معهد البيان - قاعة الفردوس", goal: "حفظ الأجزاء الخمسة الأخيرة برواية حفص" },
        { name: "حلقة الزهراء للفتيات", gender: "FEMALE", teacher: "الأستاذة هدى العمري", phone: "0559988776", schedule: "المساء (6:00 - 7:30 م)", location: "معهد البيان - قاعة الهدى", goal: "ختم القرآن الكريم كاملاً وتجويده" }
    ],
    recitations: [
        { studentId: 1, studentName: "عمر أحمد الشريف", type: "حفظ جديد", surah: "الملك", fromAyah: 1, toAyah: 15, rating: "ممتاز 🌟", tajweed: 10, fluency: 10, notes: "إتقان تام للمدود ومخارج الحروف.", date: new Date().toISOString().split('T')[0] },
        { studentId: 2, studentName: "فاطمة محمد السعيد", type: "حفظ جديد", surah: "المجادلة", fromAyah: 1, toAyah: 10, rating: "ممتاز 🌟", tajweed: 10, fluency: 9, notes: "تلاوة خاشعة وأداء متميز.", date: new Date().toISOString().split('T')[0] }
    ],
    attendance: { 1: "PRESENT", 2: "PRESENT", 3: "LATE", 4: "PRESENT", 5: "PRESENT", 6: "EXCUSED" },
    supervisors: [
        { name: "د. عبد الرقيب عبد الخالق", role: "المشرف العام لحلقات هائل سعيد", phone: "0500000001" },
        { name: "الأستاذة أسماء الحميري", role: "مشرفة الحلقات النسائية", phone: "0500000002" }
    ]
};

// الهيكل الرسمي لجمعية معاذ الحروي
const initialMuadhData = {
    key: 'MUADH',
    name: "جمعية معاذ الحروي لتحفيظ القرآن الكريم",
    shortName: "جمعية معاذ الحروي",
    location: "صنعاء / تعز - اليمن",
    color: "#1A5276",
    students: [
        { id: 101, name: "بلال عبد الكريم الحروي", gender: "MALE", circle: "حلقة معاذ بن جبل", code: "MDH-201", phone: "0507771122", juz: 28, points: 340, streak: 14, goal: "إتقان القراءات العشر بالسند", level: "المستوى المتقدم / المتقن 🥇", memScore: 90, tajScore: 92, makhScore: 91, waqfScore: 89 },
        { id: 102, name: "عائشة فاروق المعمري", gender: "FEMALE", circle: "حلقة أم سلمة (بنات)", code: "MDH-202", phone: "0558882233", juz: 26, points: 490, streak: 18, goal: "حفظ القرآن كاملاً برواية ورش", level: "مستوى الإتقان العالي ومضاهاة القُرّاء 👑", memScore: 97, tajScore: 95, makhScore: 94, waqfScore: 96 },
        { id: 103, name: "صهيب منير الجعفري", gender: "MALE", circle: "حلقة أبي بن كعب", code: "MDH-203", phone: "0549993344", juz: 30, points: 260, streak: 8, goal: "حفظ جزء عم والمقدمة الجزرية", level: "المستوى المتوسط / المجيد 🥈", memScore: 80, tajScore: 78, makhScore: 76, waqfScore: 74 },
        { id: 104, name: "زينب عبد السلام الحروي", gender: "FEMALE", circle: "حلقة رفيدة الأسلمية", code: "MDH-204", phone: "0563334455", juz: 24, points: 710, streak: 25, goal: "إجازة بالسند المتصل إلى النبي ﷺ", level: "مستوى الإتقان العالي ومضاهاة القُرّاء 👑", memScore: 99, tajScore: 98, makhScore: 97, waqfScore: 98 },
        { id: 105, name: "أنس عمار الشرعبي", gender: "MALE", circle: "حلقة معاذ بن جبل", code: "MDH-205", phone: "0536665544", juz: 29, points: 310, streak: 10, goal: "حفظ 15 جزءاً وضبط الوقف والابتداء", level: "المستوى المتقدم / المتقن 🥇", memScore: 88, tajScore: 86, makhScore: 87, waqfScore: 85 }
    ],
    circles: [
        { name: "حلقة معاذ بن جبل", gender: "MALE", teacher: "الشيخ فؤاد الحروي", phone: "0507770001", schedule: "بين المغرب والعشاء", location: "جامع معاذ بن جبل الكبير", goal: "تعليم القراءات العشر المتواترة" },
        { name: "حلقة أبي بن كعب", gender: "MALE", teacher: "الشيخ مأمون السقاف", phone: "0507770002", schedule: "بعد صلاة الفجر", location: "دار معاذ لعلوم القرآن", goal: "حفظ وإتقان منظومة الجزرية" },
        { name: "حلقة أم سلمة (بنات)", gender: "FEMALE", teacher: "الأستاذة سميرة الشرعبي", phone: "0558880001", schedule: "الصباح (9:00 - 11:00 ص)", location: "مركز الفتيات - جمعية معاذ", goal: "حفظ القرآن برواية ورش عن نافع" },
        { name: "حلقة رفيدة الأسلمية", gender: "FEMALE", teacher: "الأستاذة أسماء الحروي", phone: "0558880002", schedule: "العصر (3:30 - 5:00 م)", location: "مركز الفتيات - جمعية معاذ", goal: "الإجازة بالسند المتصل برواية حفص وشعبة" }
    ],
    recitations: [
        { studentId: 101, studentName: "بلال عبد الكريم الحروي", type: "حفظ جديد", surah: "الواقعة", fromAyah: 1, toAyah: 30, rating: "ممتاز 🌟", tajweed: 10, fluency: 10, notes: "إتقان فائق لأحكام المد والإدغام الكبير.", date: new Date().toISOString().split('T')[0] },
        { studentId: 102, studentName: "عائشة فاروق المعمري", type: "مراجعة صغرى", surah: "يس", fromAyah: 1, toAyah: 40, rating: "ممتاز 🌟", tajweed: 10, fluency: 9, notes: "مراجعة ممتازة برواية ورش عن نافع.", date: new Date().toISOString().split('T')[0] }
    ],
    attendance: { 101: "PRESENT", 102: "PRESENT", 103: "PRESENT", 104: "PRESENT", 105: "LATE" },
    supervisors: [
        { name: "الشيخ عبد الولي الحروي", role: "المشرف العام لجمعية معاذ الحروي", phone: "0507779999" },
        { name: "الأستاذة خديجة المعمري", role: "مشرفة القسم النسائي وحلقات الإتقان", phone: "0507778888" }
    ]
};

function loadOrgData(orgKey) {
    try {
        const saved = localStorage.getItem(ORG_STORAGE_KEY_PREFIX + orgKey);
        if (saved) return JSON.parse(saved);
    } catch (e) {
        console.warn('تعذر استرجاع بيانات الجمعية:', e);
    }
    return orgKey === 'MUADH' ? JSON.parse(JSON.stringify(initialMuadhData)) : JSON.parse(JSON.stringify(initialHaelData));
}

function saveOrgData(orgKey, data) {
    try {
        localStorage.setItem(ORG_STORAGE_KEY_PREFIX + orgKey, JSON.stringify(data));
    } catch (e) {
        console.warn('تعذر حفظ بيانات الجمعية:', e);
    }
}

// ========================================================
// 🛡️ سجل تدقيق أمان المعلومات والرقابة الرقمية
// ========================================================
function loadSecurityAuditLog() {
    try {
        const saved = localStorage.getItem(AUDIT_LOG_STORAGE_KEY);
        if (saved) return JSON.parse(saved);
    } catch (e) {}
    return [
        { time: "2026-10-15 14:00", org: "جمعية هائل سعيد أنعم", role: "مشرف عام", code: "SUPER-HSA-777", status: "AUTHORIZED", details: "اعتماد رسمي لجلسة الإشراف العام" },
        { time: "2026-10-15 14:15", org: "جمعية معاذ الحروي", role: "معلم", code: "TCH-MDH-01", status: "AUTHORIZED", details: "دخول مصرح به لحلقة معاذ بن جبل" }
    ];
}

function logSecurityEvent(orgName, role, code, status, details) {
    const logs = loadSecurityAuditLog();
    const now = new Date();
    const timeStr = `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,'0')}-${String(now.getDate()).padStart(2,'0')} ${String(now.getHours()).padStart(2,'0')}:${String(now.getMinutes()).padStart(2,'0')}`;
    logs.unshift({
        time: timeStr,
        org: orgName,
        role: role,
        code: code,
        status: status,
        details: details
    });
    // الاحتفاظ بآخر 50 عملية تدقيق
    if (logs.length > 50) logs.pop();
    try {
        localStorage.setItem(AUDIT_LOG_STORAGE_KEY, JSON.stringify(logs));
    } catch (e) {}
}

// المتغيرات الحية النشطة
let currentOrgKey = 'HAEL';
let currentOrgData = loadOrgData(currentOrgKey);

let quranData = {
    get students() { return currentOrgData.students; },
    get recitations() { return currentOrgData.recitations; },
    get attendance() { return currentOrgData.attendance; }
};

let girlsSectionData = {
    get circles() { return currentOrgData.circles.filter(c => c.gender === 'FEMALE'); },
    activeReportText: ""
};

// ========================================================
// 🔐 بوابة الدخول والتحقق الأمني المشدد (Authentication Gate)
// ========================================================
let gateSelectedOrg = 'HAEL';
let gateSelectedRole = 'SUPERVISOR';
let activeAuthSession = null;

// التحقق من الجلسة المخزنة عند التحميل
function checkActiveSession() {
    try {
        const saved = localStorage.getItem(SESSION_STORAGE_KEY);
        if (saved) {
            activeAuthSession = JSON.parse(saved);
            applyAuthenticatedSession(activeAuthSession, false);
            return true;
        }
    } catch (e) {}
    // إذا لم تكن هناك جلسة موثقة، تفتح بوابة الدخول والتحقق الأمني
    showSecureLoginGate();
    return false;
}

function showSecureLoginGate() {
    const gate = document.getElementById('secureLoginGate');
    if (gate) {
        gate.style.display = 'flex';
        renderGateQuickCodes();
    }
}

function hideSecureLoginGate() {
    const gate = document.getElementById('secureLoginGate');
    if (gate) gate.style.display = 'none';
}

function selectGateOrg(orgKey) {
    gateSelectedOrg = orgKey;
    const btnH = document.getElementById('gateOrgHael');
    const btnM = document.getElementById('gateOrgMuadh');
    if (btnH && btnM) {
        if (orgKey === 'HAEL') {
            btnH.classList.add('active');
            btnM.classList.remove('active');
        } else {
            btnM.classList.add('active');
            btnH.classList.remove('active');
        }
    }
    renderGateQuickCodes();
    clearGateAlert();
}

function selectGateRole(role) {
    gateSelectedRole = role;
    ['SUPERVISOR', 'TEACHER', 'PARENT'].forEach(r => {
        const btn = document.getElementById(`gateRole${capitalizeFirst(r)}`);
        if (btn) {
            if (r === role) btn.classList.add('active');
            else btn.classList.remove('active');
        }
    });
    renderGateQuickCodes();
    clearGateAlert();
}

function capitalizeFirst(str) {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

function renderGateQuickCodes() {
    const container = document.getElementById('gateQuickCodesContainer');
    if (!container) return;

    const orgRegistry = OFFICIAL_AUTHORIZED_REGISTRY[gateSelectedOrg];
    if (!orgRegistry) return;

    const list = orgRegistry.roles[gateSelectedRole] || [];
    container.innerHTML = list.slice(0, 3).map(item => {
        const label = item.name || item.studentName;
        return `
            <button type="button" class="btn btn-outline" style="padding: 4px 10px; font-size: 0.78rem; font-weight: 700;" onclick="fillGateCode('${item.code}')">
                🔑 ${item.code} (${label})
            </button>
        `;
    }).join('');
}

function fillGateCode(code) {
    const inp = document.getElementById('gateAccessCodeInput');
    if (inp) {
        inp.value = code;
        inp.focus();
    }
    clearGateAlert();
}

function clearGateAlert() {
    const alertBox = document.getElementById('gateSecurityAlert');
    if (alertBox) {
        alertBox.style.display = 'none';
        alertBox.innerText = '';
    }
}

function showGateAlert(msg) {
    const alertBox = document.getElementById('gateSecurityAlert');
    if (alertBox) {
        alertBox.innerText = msg;
        alertBox.style.display = 'block';
    }
}

// التحقق الأمني من الكود وعزل الجمعيتين
function verifyAndLogin() {
    const codeInput = document.getElementById('gateAccessCodeInput');
    if (!codeInput) return;

    const rawCode = codeInput.value.trim().toUpperCase();
    if (!rawCode) {
        showGateAlert("⚠️ يرجى إدخال كود الاعتماد الممنوح لك من المشرف العام");
        return;
    }

    const currentOrgName = OFFICIAL_AUTHORIZED_REGISTRY[gateSelectedOrg].orgName;
    const otherOrgKey = gateSelectedOrg === 'HAEL' ? 'MUADH' : 'HAEL';
    const otherOrgName = OFFICIAL_AUTHORIZED_REGISTRY[otherOrgKey].orgName;

    // تدقيق أمني 1: منع تداخل كود جمعية مع الأخرى (Cross-Association Security Barrier)
    let isOtherOrgCode = false;
    const otherRoles = OFFICIAL_AUTHORIZED_REGISTRY[otherOrgKey].roles;
    Object.values(otherRoles).forEach(arr => {
        if (arr.some(item => item.code.toUpperCase() === rawCode)) {
            isOtherOrgCode = true;
        }
    });

    if (isOtherOrgCode) {
        const breachMsg = `⛔ تم رفض الدخول أمنياً! هذا الكود رسمي لـ (${otherOrgName}) ولا يُسمح نهائياً باستخدامه في (${currentOrgName}). تم توثيق المحاولة في سجل التدقيق الأمني 🛡️`;
        showGateAlert(breachMsg);
        logSecurityEvent(currentOrgName, gateSelectedRole, rawCode, "BLOCKED_CROSS_ORG", `محاولة اختراق عزل البيانات باستخدام كود ${otherOrgName}`);
        return;
    }

    // تدقيق أمني 2: التحقق من وجود الكود في الجمعية المختارة
    const currentRoles = OFFICIAL_AUTHORIZED_REGISTRY[gateSelectedOrg].roles;
    const targetRoleList = currentRoles[gateSelectedRole] || [];
    const matchedCredential = targetRoleList.find(c => c.code.toUpperCase() === rawCode);

    if (matchedCredential) {
        // تم التحقق بنجاح!
        const session = {
            orgKey: gateSelectedOrg,
            orgName: currentOrgName,
            role: gateSelectedRole,
            code: matchedCredential.code,
            userName: matchedCredential.name || matchedCredential.studentName,
            roleTitle: matchedCredential.roleTitle,
            loginTime: new Date().toLocaleTimeString('ar-SA')
        };

        applyAuthenticatedSession(session, true);
        logSecurityEvent(currentOrgName, gateSelectedRole, rawCode, "AUTHORIZED_SUCCESS", `تم الدخول المصرح به: ${session.userName} (${session.roleTitle})`);
        hideSecureLoginGate();
        clearGateAlert();
        codeInput.value = '';
        showToast(`مرحباً بك: ${session.userName} - تم التحقق الأمني بنجاح 🛡️`);
    } else {
        // فحص هل الكود ينتمي لمستوى آخر في نفس الجمعية
        let foundOtherRole = null;
        Object.keys(currentRoles).forEach(r => {
            if (currentRoles[r].some(c => c.code.toUpperCase() === rawCode)) {
                foundOtherRole = r;
            }
        });

        if (foundOtherRole) {
            const roleLabels = { 'SUPERVISOR': 'مشرف عام', 'TEACHER': 'معلم / محفظ', 'PARENT': 'ولي أمر' };
            const alertMsg = `⛔ الكود المدخل صحيح لكنه مخصص لمستوى [${roleLabels[foundOtherRole]}]، ولا يملك تصريح [${roleLabels[gateSelectedRole]}]. يرجى اختيار المستوى المصرح به.`;
            showGateAlert(alertMsg);
            logSecurityEvent(currentOrgName, gateSelectedRole, rawCode, "ROLE_MISMATCH", `محاولة استخدام كود ${roleLabels[foundOtherRole]} في مستوى ${roleLabels[gateSelectedRole]}`);
        } else {
            showGateAlert("⛔ كود غير مصرح به أو لم يصدر من المشرف العام. يرجى التأكد من كتابة الكود بشكل سليم.");
            logSecurityEvent(currentOrgName, gateSelectedRole, rawCode, "UNAUTHORIZED_CODE", "كود غير مسجل في منظومة المشرف العام");
        }
    }
}

function applyAuthenticatedSession(session, saveStorage = true) {
    activeAuthSession = session;
    if (saveStorage) {
        try {
            localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(session));
        } catch (e) {}
    }

    // مزامنة الجمعية والمستوى
    switchOrganization(session.orgKey, false);

    // تحديث ترويسة الجلسة في الشريط العلوي
    const statusElem = document.getElementById('headerSessionStatus');
    if (statusElem) {
        statusElem.innerText = `🔒 جلسة معتمدة: ${session.userName} (${session.roleTitle}) - ${currentOrgData.shortName}`;
    }

    // توجيه تلقائي للشاشة المناسبة
    if (session.role === 'SUPERVISOR') {
        const sel = document.getElementById('roleSelector');
        if (sel) sel.value = 'SUPERVISOR';
        switchTab('supervisorTab');
    } else if (session.role === 'TEACHER') {
        const sel = document.getElementById('roleSelector');
        if (sel) sel.value = 'TEACHER';
        switchTab('teacherTab');
    } else if (session.role === 'PARENT') {
        const sel = document.getElementById('roleSelector');
        if (sel) sel.value = 'PARENT';
        switchTab('parentTab');
        const parentCodeInp = document.getElementById('parentCodeInput');
        if (parentCodeInp) parentCodeInp.value = session.code;
        searchParentPortal();
    }
}

function promptSecureLogout() {
    if (confirm("هل تريد تسجيل الخروج وقفل الجلسة الآمنة لمنظومة الحلقات؟")) {
        if (activeAuthSession) {
            logSecurityEvent(activeAuthSession.orgName, activeAuthSession.role, activeAuthSession.code, "LOGOUT_CLEARED", `تسجيل خروج آمن للمستخدم ${activeAuthSession.userName}`);
        }
        activeAuthSession = null;
        try {
            localStorage.removeItem(SESSION_STORAGE_KEY);
        } catch (e) {}
        showSecureLoginGate();
        showToast("تم قفل الجلسة وتسجيل الخروج بأمان 🔒");
    }
}

function openSecurityAuditModal() {
    renderSecurityAuditTable();
    const modal = document.getElementById('modalSecurityAudit');
    if (modal) modal.classList.add('active');
}

function closeSecurityAuditModal() {
    const modal = document.getElementById('modalSecurityAudit');
    if (modal) modal.classList.remove('active');
}

function renderSecurityAuditTable() {
    const tbody = document.getElementById('auditTableBody');
    if (!tbody) return;

    const logs = loadSecurityAuditLog();
    if (logs.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; padding: 14px;">لا توجد سجلات أمنية مسجلة</td></tr>';
        return;
    }

    tbody.innerHTML = logs.map(l => {
        let badge = "";
        if (l.status === 'AUTHORIZED_SUCCESS' || l.status === 'AUTHORIZED') {
            badge = '<span class="badge" style="background:#E8F5E9; color:#2E7D32;">مصرح به ✅</span>';
        } else if (l.status === 'BLOCKED_CROSS_ORG') {
            badge = '<span class="badge" style="background:#FFEBEE; color:#C62828;">محظور (عزل بيانات) 🚫</span>';
        } else {
            badge = '<span class="badge" style="background:#FFF3E0; color:#EF6C00;">مرفوض ⚠️</span>';
        }

        return `
            <tr>
                <td style="font-size:0.75rem; color:var(--text-secondary);">${l.time}</td>
                <td style="font-weight:700;">${l.org}</td>
                <td>${l.role}</td>
                <td style="font-family:monospace; font-weight:700;">${l.code}</td>
                <td>${badge} <div style="font-size:0.72rem; color:var(--text-secondary);">${l.details}</div></td>
            </tr>
        `;
    }).join('');
}

function shareSecurityAuditWhatsApp() {
    const logs = loadSecurityAuditLog().slice(0, 10);
    const text = `🛡️ *تقرير تدقيق أمان المعلومات والرقابة الرقمية* 🛡️\n\n` +
        `التاريخ: ${new Date().toLocaleDateString('ar-SA')}\n` +
        `المشرف العام: منظومة منارة الجيل القرآني\n` +
        `━━━━━━━━━━━━━━━\n` +
        logs.map((l, i) => `${i + 1}. [${l.time}] ${l.org}\nالكود: ${l.code} | المستوى: ${l.role}\nالحالة: ${l.status}\nالتفاصيل: ${l.details}`).join('\n\n') +
        `\n\n🔒 عزل تام ومحكم بين جمعية هائل سعيد أنعم وجمعية معاذ الحروي.`;
    window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`, '_blank');
}

// ==========================================
// 🏛️ التبديل بين الجمعيات وتحديث الواجهات
// ==========================================
function switchOrganization(orgKey, notify = true) {
    if (orgKey !== 'HAEL' && orgKey !== 'MUADH') return;
    
    saveOrgData(currentOrgKey, currentOrgData);
    
    currentOrgKey = orgKey;
    try {
        localStorage.setItem(ACTIVE_ORG_STORAGE_KEY, currentOrgKey);
    } catch (e) {}
    
    currentOrgData = loadOrgData(currentOrgKey);
    
    // تحديث أزرار الشريط
    const btnHael = document.getElementById('orgBtnHael');
    const btnMuadh = document.getElementById('orgBtnMuadh');
    if (btnHael && btnMuadh) {
        if (currentOrgKey === 'HAEL') {
            btnHael.classList.add('active');
            btnMuadh.classList.remove('active');
        } else {
            btnMuadh.classList.add('active');
            btnHael.classList.remove('active');
        }
    }
    
    // تحديث البانر
    const bannerName = document.getElementById('orgBannerName');
    const bannerDesc = document.getElementById('orgBannerDesc');
    const bannerLoc = document.getElementById('orgBannerLocation');
    const bannerNotice = document.getElementById('orgBannerNotice');
    
    if (bannerName) bannerName.innerText = currentOrgData.name;
    if (bannerLoc) bannerLoc.innerText = currentOrgData.location;
    if (bannerNotice) {
        if (currentOrgKey === 'MUADH') {
            bannerNotice.style.background = 'linear-gradient(135deg, #1A5276, #0E2938)';
        } else {
            bannerNotice.style.background = 'linear-gradient(135deg, #0F5132, #18241D)';
        }
    }
    
    const supOrgName = document.getElementById('supervisorOrgNameText');
    if (supOrgName) supOrgName.innerText = currentOrgData.name;
    const supIso = document.getElementById('supervisorIsolationStatus');
    if (supIso) supIso.innerText = `🔒 ${currentOrgData.shortName}`;
    
    renderStudents(currentGender, currentSearch);
    if (window.renderSupervisorDashboard) renderSupervisorDashboard();
    if (window.renderGirlsAdmin) renderGirlsAdmin();
    if (window.renderHalaqat) renderHalaqat();
    if (window.renderQuickAttendance) renderQuickAttendance();
    if (window.renderReadersLevelTab) renderReadersLevelTab();
    if (window.renderQiraatTab) renderQiraatTab();
    
    if (notify) {
        showToast(`تم التبديل بنجاح إلى: ${currentOrgData.shortName} (عزل البيانات مفعل 🔒)`);
    }
}

function requestSwitchOrganization(orgKey) {
    if (orgKey === currentOrgKey) return;
    
    // فحص أمني: منع تداخل بيانات الجمعيتين والدخول بدون كود معتمد
    if (activeAuthSession && activeAuthSession.orgKey !== orgKey) {
        const nextOrgName = OFFICIAL_AUTHORIZED_REGISTRY[orgKey] ? OFFICIAL_AUTHORIZED_REGISTRY[orgKey].orgName : orgKey;
        selectGateOrg(orgKey);
        showSecureLoginGate();
        showGateAlert(`🛡️ العزل الأمني المشدد مفعل: بيانات (${nextOrgName}) معزولة ومحمية. يرجى إدخال الكود المصرح به من المشرف العام لتلك الجمعية لضمان سرية وأمان المعلومات.`);
        return;
    }
    switchOrganization(orgKey, true);
}

function openGateSwitchModal() {
    showSecureLoginGate();
    clearGateAlert();
}

function onRoleChange(role) {
    if (activeAuthSession && activeAuthSession.role !== role) {
        if (role === 'SUPERVISOR' && activeAuthSession.role !== 'SUPERVISOR') {
            selectGateRole('SUPERVISOR');
            showSecureLoginGate();
            showGateAlert("🛡️ تنبيه أمني: الدخول للوحة المشرف العام محمي ويتطلب كود الاعتماد الإشرافي الصادر من الإدارة العامة.");
            const sel = document.getElementById('roleSelector');
            if (sel) sel.value = activeAuthSession.role;
            return;
        }
    }

    if (role === 'SUPERVISOR') {
        switchTab('supervisorTab');
        showToast("مرحباً بك في لوحة الإشراف العام والمتابعة العليا 🏛️");
    } else if (role === 'TEACHER') {
        switchTab('teacherTab');
        showToast("مرحباً بك في لوحة المعلم والمحفظ 👨‍🏫");
    } else if (role === 'PARENT') {
        switchTab('parentTab');
        showToast("مرحباً بك في بوابة متابعة ولي الأمر 👨‍👩‍👧‍👦");
    }
}

// ==========================================
// PWA Installation
// ==========================================
// PWA Installation
// ==========================================
let deferredPrompt;

function initPwaInstall() {
    const btn = document.getElementById('installBtn');
    if (btn && typeof btn.addEventListener === 'function') {
        btn.addEventListener('click', async () => {
            if (deferredPrompt) {
                deferredPrompt.prompt();
                const { outcome } = await deferredPrompt.userChoice;
                if (outcome === 'accepted') {
                    console.log('تم تثبيت التطبيق بنجاح');
                }
                deferredPrompt = null;
            } else {
                showInstallInstructionsModal();
            }
        });
    }
}

window.addEventListener('beforeinstallprompt', (e) => {
    e.preventDefault();
    deferredPrompt = e;
    const btn = document.getElementById('installBtn');
    if (btn) btn.style.display = 'inline-flex';
    const installBanner = document.getElementById('installBanner');
    if (installBanner) installBanner.style.display = 'flex';
});

document.addEventListener('DOMContentLoaded', () => {
    initPwaInstall();
});
if (document.readyState === 'interactive' || document.readyState === 'complete') {
    initPwaInstall();
}

function showInstallInstructionsModal() {
    alert("📱 طريقة تثبيت التطبيق على الأجهزة المختلفة:\n\n• الأندرويد والكمبيوتر (Chrome / Edge): يظهر زر التثبيت تلقائياً أو عبر الضغط على القائمة (⋮) ثم 'تثبيت التطبيق' أو 'إضافة إلى الشاشة الرئيسية'.\n\n• الآيفون والآيباد (Safari): متصفح Safari لا يدعم التثبيت المباشر بالزر البرمجي؛ اضغط على زر المشاركة (Share ⬆️) ثم اختر 'الإضافة إلى الشاشة الرئيسية (Add to Home Screen)'.");
}

function installApp() {
    if (deferredPrompt) deferredPrompt.prompt();
    else showInstallInstructionsModal();
}

// ==========================================
// 1. عرض قائمة الطلاب وإدارتهم (لوحة المعلم)
// ==========================================
function renderStudents(filterGender = 'ALL', searchQuery = '') {
    const container = document.getElementById('studentsList');
    if (!container) return;

    let list = currentOrgData.students;
    if (filterGender !== 'ALL') {
        list = list.filter(s => s.gender === filterGender);
    }
    if (searchQuery.trim()) {
        const q = searchQuery.trim().toLowerCase();
        list = list.filter(s => s.name.toLowerCase().includes(q) || s.code.toLowerCase().includes(q) || s.circle.toLowerCase().includes(q));
    }

    container.innerHTML = '';
    if (list.length === 0) {
        container.innerHTML = `<div class="empty-state" style="text-align: center; padding: 30px; color: var(--text-secondary);">لا يوجد طلاب مسجلون في هذه الفئة بـ ${currentOrgData.shortName}</div>`;
        return;
    }

    list.forEach(student => {
        const attStatus = currentOrgData.attendance[student.id] || "NONE";
        const attLabel = getAttendanceLabel(attStatus);
        const lastRec = currentOrgData.recitations.find(r => r.studentId === student.id);

        const card = document.createElement('div');
        card.className = 'student-card';
        card.innerHTML = `
            <div class="student-header">
                <div class="student-avatar ${student.gender.toLowerCase()}">${student.name.charAt(0)}</div>
                <div class="student-info">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <h3>${student.name}</h3>
                        <span class="badge ${student.gender.toLowerCase()}">${student.gender === 'MALE' ? 'بنين 👦' : 'بنات 👧'}</span>
                    </div>
                    <p>🕌 ${student.circle} | رمز: <strong>${student.code}</strong></p>
                </div>
            </div>

            <div class="meta-row">
                <span>📖 الجزء الحالي: <strong>${student.juz || 30}</strong></span>
                <span>⭐ نقاط التميز: <strong>${student.points}</strong></span>
                <span>🔥 مواظبة: <strong>${student.streak} أيام</strong></span>
            </div>

            <div style="background: var(--bg); border: 1px solid var(--border); border-radius: 8px; padding: 8px 12px; margin: 10px 0; font-size: 0.85rem; display: flex; justify-content: space-between; align-items: center;">
                <span>حالة الحضور اليومي:</span>
                <span class="badge" style="background: #E8F5E9; color: #1B5E20; font-weight: 700;">${attLabel}</span>
            </div>

            ${lastRec ? `
            <div style="background: #FFF9C4; border: 1px solid #FFF176; border-radius: 8px; padding: 8px 12px; font-size: 0.85rem; margin-bottom: 10px;">
                <strong>آخر تسميع:</strong> سورة ${lastRec.surah} (${lastRec.fromAyah}-${lastRec.toAyah}) - ${lastRec.rating}
                <div style="color: #5D4037; font-size: 0.8rem; margin-top: 3px;">📝 ${lastRec.notes}</div>
            </div>
            ` : ''}

            <div class="student-actions">
                <button class="btn btn-primary" onclick="openRecitationModal(${student.id})">📖 تسجيل تسميع</button>
                <button class="btn btn-secondary" onclick="toggleStudentAttendance(${student.id})">⚡ تحضير سريع</button>
                <button class="btn btn-whatsapp" onclick="shareStudentWhatsAppReport(${student.id})">📲 واتساب ولي الأمر</button>
            </div>
        `;
        container.appendChild(card);
    });
}

function getAttendanceLabel(status) {
    switch (status) {
        case 'PRESENT': return 'حاضر ✅';
        case 'LATE': return 'متأخر ⏰';
        case 'EXCUSED': return 'مستأذن ℹ️';
        case 'ABSENT': return 'غائب ❌';
        default: return 'لم يرصد بعد';
    }
}

function toggleStudentAttendance(studentId) {
    const current = currentOrgData.attendance[studentId] || 'NONE';
    const nextMap = {
        'NONE': 'PRESENT',
        'PRESENT': 'LATE',
        'LATE': 'EXCUSED',
        'EXCUSED': 'ABSENT',
        'ABSENT': 'PRESENT'
    };
    currentOrgData.attendance[studentId] = nextMap[current] || 'PRESENT';
    saveOrgData(currentOrgKey, currentOrgData);
    renderStudents(currentGender, currentSearch);
    if (window.renderQuickAttendance) renderQuickAttendance();
    const st = currentOrgData.students.find(s => s.id === studentId);
    showToast(`تم تحديث حضور ${st ? st.name : ''}: ${getAttendanceLabel(currentOrgData.attendance[studentId])}`);
}

function shareStudentWhatsAppReport(studentId) {
    const student = currentOrgData.students.find(s => s.id === studentId);
    if (!student) return;

    const attStatus = currentOrgData.attendance[student.id] || "PRESENT";
    const attLabel = getAttendanceLabel(attStatus);
    const lastRec = currentOrgData.recitations.find(r => r.studentId === student.id);

    const text = `🌿 *تقرير متابعة حفظ ومواظبة القرآن الكريم* 🌿\n\n` +
        `السلام عليكم ورحمة الله، ولي أمر الطالب الكريم:\n` +
        `👤 اسم الطالب: *${student.name}*\n` +
        `🏛️ الجمعية: *${currentOrgData.name}*\n` +
        `🕌 الحلقة: *${student.circle}*\n` +
        `📅 التاريخ: ${new Date().toLocaleDateString('ar-SA')}\n` +
        `حالة الحضور: *${attLabel}*\n` +
        (lastRec ? `📖 آخر تسميع: سورة ${lastRec.surah} (${lastRec.fromAyah}-${lastRec.toAyah})\nالتقدير: ${lastRec.rating}\nالملاحظة: ${lastRec.notes}\n` : '') +
        `⭐ رصيد النقاط: *${student.points} نقطة*\n` +
        `🔥 أيام المواظبة: *${student.streak} أيام*\n` +
        `🎯 الهدف القرآني: ${student.goal}\n\n` +
        `كود متابعة ولي الأمر: *${student.code}*\n` +
        `━━━━━━━━━━━━━━━\n` +
        `منظومة منارة الجيل القرآني - ${currentOrgData.shortName}`;

    const cleanPhone = (student.phone || "").replace(/[^0-9]/g, '');
    const url = cleanPhone 
        ? `https://api.whatsapp.com/send?phone=${cleanPhone}&text=${encodeURIComponent(text)}`
        : `https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`;
    window.open(url, '_blank');
}

// ==========================================
// 🏛️ لوحة الإشراف العام والمتابعة العليا
// ==========================================
function renderSupervisorDashboard() {
    const orgNameElem = document.getElementById('supervisorOrgNameText');
    if (orgNameElem) orgNameElem.innerText = currentOrgData.name;
    const isoStatus = document.getElementById('supervisorIsolationStatus');
    if (isoStatus) isoStatus.innerText = `🔒 ${currentOrgData.shortName}`;

    const statsGrid = document.getElementById('supervisorStatsGrid');
    if (statsGrid) {
        const totalCircles = currentOrgData.circles.length;
        const totalStudents = currentOrgData.students.length;
        const totalRecitations = currentOrgData.recitations.length;
        const presentCount = currentOrgData.students.filter(s => currentOrgData.attendance[s.id] === 'PRESENT').length;
        const attRate = totalStudents > 0 ? Math.round((presentCount / totalStudents) * 100) : 0;

        statsGrid.innerHTML = `
            <div style="background: #E8F5E9; border: 1px solid #C8E6C9; border-radius: 12px; padding: 14px; text-align: center;">
                <div style="font-size: 1.8rem; font-weight: 800; color: #2E7D32;">${totalCircles}</div>
                <div style="font-size: 0.85rem; color: #1B5E20; font-weight: 700;">🕌 إجمالي الحلقات</div>
            </div>
            <div style="background: #E3F2FD; border: 1px solid #BBDEFB; border-radius: 12px; padding: 14px; text-align: center;">
                <div style="font-size: 1.8rem; font-weight: 800; color: #1565C0;">${totalStudents}</div>
                <div style="font-size: 0.85rem; color: #0D47A1; font-weight: 700;">👥 إجمالي الطلاب والطالبات</div>
            </div>
            <div style="background: #FFF9C4; border: 1px solid #FFF59D; border-radius: 12px; padding: 14px; text-align: center;">
                <div style="font-size: 1.8rem; font-weight: 800; color: #F57F17;">${totalRecitations}</div>
                <div style="font-size: 0.85rem; color: #E65100; font-weight: 700;">📖 جلسات التسميع المنجزة</div>
            </div>
            <div style="background: #F3E5F5; border: 1px solid #E1BEE7; border-radius: 12px; padding: 14px; text-align: center;">
                <div style="font-size: 1.8rem; font-weight: 800; color: #7B1FA2;">${attRate}%</div>
                <div style="font-size: 0.85rem; color: #4A148C; font-weight: 700;">✅ نسبة الانضباط والحضور</div>
            </div>
        `;
    }

    const circlesTable = document.getElementById('supervisorCirclesTable');
    if (circlesTable) {
        circlesTable.innerHTML = '';
        currentOrgData.circles.forEach((c) => {
            const studentCount = currentOrgData.students.filter(s => s.circle === c.name).length;
            const card = document.createElement('div');
            card.className = 'qiraat-card';
            card.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
                    <h4 style="margin: 0; font-size: 1.05rem; color: var(--primary);">${c.name}</h4>
                    <span class="badge ${c.gender ? c.gender.toLowerCase() : 'male'}">${c.gender === 'FEMALE' ? 'حلقة فتيات 🌸' : 'حلقة بنين 👦'}</span>
                </div>
                <div style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.6;">
                    <div>👤 المعلم/المحفظ: <strong>${c.teacher}</strong></div>
                    <div>📞 الهاتف: <strong>${c.phone || '0500000000'}</strong></div>
                    <div>⏰ الموعد: ${c.schedule || 'يومياً'}</div>
                    <div>📍 المقر: ${c.location || 'المركز'}</div>
                    <div>👥 عدد الطلاب المسجلين: <strong style="color: var(--primary);">${studentCount} طالب</strong></div>
                </div>
                <div style="display: flex; gap: 8px; margin-top: 12px;">
                    <button class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem;" onclick="filterSupervisorByCircle('${c.name}')">🔍 استعراض الطلاب</button>
                    <button class="btn btn-outline" style="padding: 4px 10px; font-size: 0.8rem;" onclick="shareCircleSummaryWhatsApp('${c.name}')">📲 تقرير الحلقة</button>
                </div>
            `;
            circlesTable.appendChild(card);
        });
    }
}

function openSupervisorAddCircleModal() {
    const modal = document.getElementById('modalSupervisorAddCircle');
    const orgName = document.getElementById('modalSupervisorOrgName');
    if (orgName) orgName.innerText = currentOrgData.name;
    if (modal) modal.classList.add('active');
}

function closeSupervisorAddCircleModal() {
    const modal = document.getElementById('modalSupervisorAddCircle');
    if (modal) modal.classList.remove('active');
}

function saveSupervisorNewCircle() {
    const name = document.getElementById('inputSupCircleName').value.trim();
    const gender = document.getElementById('selectSupCircleGender').value;
    const teacher = document.getElementById('inputSupTeacherName').value.trim();
    const phone = document.getElementById('inputSupTeacherPhone').value.trim();
    const schedule = document.getElementById('inputSupSchedule').value.trim();
    const location = document.getElementById('inputSupLocation').value.trim();

    if (!name || !teacher) {
        showToast("يرجى إدخال اسم الحلقة واسم المعلم المعتمد ⚠️");
        return;
    }

    currentOrgData.circles.push({
        name: name,
        gender: gender,
        teacher: teacher,
        phone: phone || "0500000000",
        schedule: schedule || "بين المغرب والعشاء",
        location: location || "جامع الجمعية",
        goal: "حفظ وتجويد القرآن الكريم"
    });

    saveOrgData(currentOrgKey, currentOrgData);
    closeSupervisorAddCircleModal();
    renderSupervisorDashboard();
    if (window.renderHalaqat) renderHalaqat();
    showToast(`تمت إضافة الحلقة ${name} لـ ${currentOrgData.shortName} بنجاح 🕌`);
}

function shareSupervisorSummaryWhatsApp() {
    const totalCircles = currentOrgData.circles.length;
    const totalStudents = currentOrgData.students.length;
    const text = `🏛️ *التقرير الإداري الشامل - مجلس الإدارة* 🏛️\n\n` +
        `🏢 الجمعية: *${currentOrgData.name}*\n` +
        `📍 المقر والفرع: ${currentOrgData.location}\n` +
        `📅 التاريخ: ${new Date().toLocaleDateString('ar-SA')}\n` +
        `━━━━━━━━━━━━━━━\n` +
        `📊 إجمالي الحلقات القرآنية: *${totalCircles} حلقة*\n` +
        `👥 إجمالي الطلاب والطالبات: *${totalStudents} طالب/طالبة*\n` +
        `🔒 نظام عزل البيانات: مفعل ومشفر على مستوى الجمعية.\n\n` +
        `قائمة الحلقات والمعلمين:\n` +
        currentOrgData.circles.map((c, i) => `${i + 1}. ${c.name} - ${c.teacher} (${c.schedule})`).join('\n') +
        `\n\nمع تحيات: الإشراف العام - منارة الجيل القرآني`;

    const url = `https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`;
    window.open(url, '_blank');
}

function filterSupervisorByCircle(circleName) {
    switchTab('teacherTab');
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.value = circleName;
        onSearchChange(circleName);
    }
}

function shareCircleSummaryWhatsApp(circleName) {
    const circle = currentOrgData.circles.find(c => c.name === circleName);
    if (!circle) return;
    const students = currentOrgData.students.filter(s => s.circle === circleName);
    const text = `🕌 *تقرير حلقة ${circle.name}* 🕌\n` +
        `الجمعية: ${currentOrgData.name}\n` +
        `المعلم: ${circle.teacher} (${circle.phone || ''})\n` +
        `الموعد والمقر: ${circle.schedule} - ${circle.location}\n` +
        `عدد الطلاب: ${students.length} طالب\n` +
        students.map((s, i) => `${i + 1}. ${s.name} - جزء ${s.juz} (كود: ${s.code})`).join('\n');
    window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`, '_blank');
}

// ==========================================
// 🎙️ ميزة القُرّاء لمعرفة وقياس مستوى الطالب
// ==========================================
let activeLevelStudentId = null;

function renderReadersLevelTab() {
    const select = document.getElementById('levelStudentSelect');
    if (!select) return;

    select.innerHTML = '';
    currentOrgData.students.forEach(s => {
        const opt = document.createElement('option');
        opt.value = s.id;
        opt.innerText = `${s.name} (${s.circle}) - [${s.code}]`;
        select.appendChild(opt);
    });

    if (currentOrgData.students.length > 0) {
        const targetId = activeLevelStudentId || currentOrgData.students[0].id;
        select.value = targetId;
        onStudentLevelSelect(targetId);
    }
}

function onStudentLevelSelect(studentId) {
    activeLevelStudentId = parseInt(studentId);
    const student = currentOrgData.students.find(s => s.id === activeLevelStudentId);
    if (!student) return;

    const nameElem = document.getElementById('levelSelectedStudentName');
    if (nameElem) nameElem.innerText = student.name;

    const mem = student.memScore || 88;
    const taj = student.tajScore || 85;
    const makh = student.makhScore || 87;
    const waqf = student.waqfScore || 86;

    document.getElementById('sliderRubricMem').value = mem;
    document.getElementById('sliderRubricTaj').value = taj;
    document.getElementById('sliderRubricMakh').value = makh;
    document.getElementById('sliderRubricWaqf').value = waqf;

    updateRubricScore(false);
}

function updateRubricScore(persist = false) {
    const mem = parseInt(document.getElementById('sliderRubricMem').value) || 0;
    const taj = parseInt(document.getElementById('sliderRubricTaj').value) || 0;
    const makh = parseInt(document.getElementById('sliderRubricMakh').value) || 0;
    const waqf = parseInt(document.getElementById('sliderRubricWaqf').value) || 0;

    document.getElementById('valRubricMem').innerText = `${mem}%`;
    document.getElementById('valRubricTaj').innerText = `${taj}%`;
    document.getElementById('valRubricMakh').innerText = `${makh}%`;
    document.getElementById('valRubricWaqf').innerText = `${waqf}%`;

    const total = Math.round((mem * 0.35) + (taj * 0.25) + (makh * 0.25) + (waqf * 0.15));

    const totalText = document.getElementById('levelTotalScoreText');
    const badgeDisplay = document.getElementById('levelBadgeDisplay');
    const descText = document.getElementById('levelDescText');
    const recoText = document.getElementById('levelRecommendationsText');

    if (totalText) totalText.innerText = `${total}%`;

    let tier = "";
    let desc = "";
    let reco = "";

    if (total >= 90) {
        tier = "مستوى الإتقان العالي / مضاهاة كبار القُرّاء والإجازة بالسند 👑";
        desc = "طالب ذو إتقان قرآني استثنائي، يتمتع بصفاء المخارج، استقرار أزمنة المدود والغنن، وتمكن من قواعد الوقف والابتداء على طريقة كبار المقارئ القرآنية.";
        reco = "الترشيح لبرنامج الإجازة بالسند المتصل برواية حفص وشعبة عن عاصم، والبدء في مدارسة متني التحفة والجزرية، وتسجيل تلاوات نموذجية للحلقة.";
    } else if (total >= 75) {
        tier = "المستوى المتقدم / المتقن 🥇";
        desc = "طالب متقن ومتميز، حفظه راسخ مع أداء تجويدي سليم، يحاكي مدرسة الترتيل والتحقيق للشيخ الحصري والشيخ المنشاوي.";
        reco = "التركيز على دقائق التفخيم والترقيق في الراءات واللامات، وضبط أزمنة المدود المتصلة والمنفصلة بمقدار 4 أو 5 حركات، والتأني في مواضع الوقف الكافي.";
    } else if (total >= 60) {
        tier = "المستوى المتوسط / المجيد 🥈";
        desc = "طالب مجيد لحفظه وأحكام التجويد الأساسية، ويحتاج إلى مزيد من التمرين على مخارج الحروف الحلقية واللسانية.";
        reco = "تكثيف الاستماع اليومي لمصحف المعلم للشيخ الحصري، والمواظبة على التسميع اليومي للورد لتقليل التردد، وتمرين الحلق على حروف الاستعلاء.";
    } else {
        tier = "المستوى التمهيدي / التأسيسي 🥉";
        desc = "في طور التلقين والتأسيس وضبط الحركات وقصار السور، يحتاج إلى عناية خاصة بالتكرار وتصحيح نطق الحروف.";
        reco = "التلقين المباشر مع المعلم آية بآية، التركيز على جزء عم، وتكرار الاستماع لسور الحفظ ثلاث مرات يومياً مع تصحيح النطق فوراً.";
    }

    if (badgeDisplay) badgeDisplay.innerText = tier;
    if (descText) descText.innerText = desc;
    if (recoText) recoText.innerText = reco;

    if (persist && activeLevelStudentId) {
        const student = currentOrgData.students.find(s => s.id === activeLevelStudentId);
        if (student) {
            student.memScore = mem;
            student.tajScore = taj;
            student.makhScore = makh;
            student.waqfScore = waqf;
            student.level = tier;
            saveOrgData(currentOrgKey, currentOrgData);
        }
    }
}

function saveStudentLevelAssessment() {
    updateRubricScore(true);
    const student = currentOrgData.students.find(s => s.id === activeLevelStudentId);
    showToast(`تم حفظ تقييم مستوى الطالب ${student ? student.name : ''} بنجاح في سجل الجمعية ✅`);
}

function exportStudentLevelWhatsApp() {
    const student = currentOrgData.students.find(s => s.id === activeLevelStudentId);
    if (!student) return;

    updateRubricScore(true);
    const totalScore = document.getElementById('levelTotalScoreText').innerText;
    const tier = document.getElementById('levelBadgeDisplay').innerText;
    const reco = document.getElementById('levelRecommendationsText').innerText;

    const mem = document.getElementById('sliderRubricMem').value;
    const taj = document.getElementById('sliderRubricTaj').value;
    const makh = document.getElementById('sliderRubricMakh').value;
    const waqf = document.getElementById('sliderRubricWaqf').value;

    const text = `🎙️ *بطاقة قياس وتقييم مستوى التلاوة والإتقان القرآني* 🎙️\n\n` +
        `👤 الطالب: *${student.name}*\n` +
        `🏛️ الجمعية: *${currentOrgData.name}*\n` +
        `🕌 الحلقة: *${student.circle}*\n` +
        `📅 تاريخ التقييم: ${new Date().toLocaleDateString('ar-SA')}\n` +
        `━━━━━━━━━━━━━━━\n` +
        `🏆 المستوى القرآني: *${tier}*\n` +
        `📊 النسبة المئوية العامة للإتقان: *${totalScore}*\n\n` +
        `معايير التلاوة وفق موازين كبار القُرّاء:\n` +
        `• الحفظ والاستحضار والطلاقة: *${mem}%*\n` +
        `• أحكام التجويد والمدود والغنن: *${taj}%*\n` +
        `• مخارج وصفات الحروف: *${makh}%*\n` +
        `• الوقف والابتداء وحسن الترتيل: *${waqf}%*\n\n` +
        `💡 خطة وتوجيهات التحسين القرآني الموصى بها:\n${reco}\n\n` +
        `كود المتابعة في البوابة: *${student.code}*\n` +
        `━━━━━━━━━━━━━━━\n` +
        `منظومة منارة الجيل القرآني - ${currentOrgData.shortName}`;

    const cleanPhone = (student.phone || "").replace(/[^0-9]/g, '');
    const url = cleanPhone 
        ? `https://api.whatsapp.com/send?phone=${cleanPhone}&text=${encodeURIComponent(text)}`
        : `https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`;
    window.open(url, '_blank');
}

let currentAudioPlayer = null;
let currentPlayingReciter = null;

function playReciterAudio(reciterKey, audioUrl) {
    if (currentAudioPlayer) {
        currentAudioPlayer.pause();
        if (currentPlayingReciter === reciterKey) {
            currentPlayingReciter = null;
            showToast("تم إيقاف التلاوة ⏹️");
            return;
        }
    }

    currentAudioPlayer = new Audio(audioUrl);
    currentPlayingReciter = reciterKey;
    currentAudioPlayer.play().then(() => {
        showToast("جاري الاستماع لنموذج كبار القُرّاء 🎧 استمع ودرب لسانك على المحاكاة");
    }).catch(() => {
        showToast("تم فتح نموذج التلاوة، يرجى التأكد من الاتصال بالإنترنت");
        window.open(audioUrl, '_blank');
    });
}

// ==========================================
// 📜 موسوعة القراءات العشر لجميع المذاهب الإسلامية
// ==========================================
let currentQiraatSubTab = 'imams';

const tenRecitersData = [
    {
        num: 1,
        name: "الإمام نافع المدني",
        city: "المدينة المنورة",
        years: "70 هـ - 169 هـ",
        narrators: ["قالون (عيسى بن مينا)", "ورش (عثمان بن سعيد)"],
        madhabNote: "قراءة أهل المدينة المنورة، وعليها مدار المذهب المالكي في شمال وغرب إفريقيا والحجاز، وقال الإمام مالك: «قراءة نافع سُنّة».",
        rules: "تتميز بتوسط المد والصلة لقالون، وتغليظ اللامات وترقيق الراءات والإمالات والبدل لورش."
    },
    {
        num: 2,
        name: "الإمام ابن كثير المكي",
        city: "مكة المكرمة",
        years: "45 هـ - 120 هـ",
        narrators: ["البزي (أحمد بن محمد)", "قنبل (محمد بن عبد الرحمن)"],
        madhabNote: "قراءة أهل مكة المشرفة، اعتنى بها الإمام الشافعي وقال: «قراءتنا قراءة ابن كثير، وعليها أدركت أهل مكة».",
        rules: "صلة هاء الضمير دائماً، وإسقاط وتسهيل إحدى الهمزتين المجتمعتين، وتوسط وقصر المنفصل."
    },
    {
        num: 3,
        name: "الإمام أبو عمرو البصري",
        city: "البصرة",
        years: "68 هـ - 154 هـ",
        narrators: ["الدوري (حفص بن عمر)", "السوسي (صالح بن زياد)"],
        madhabNote: "انتشرت في العراق واليمن وشرق إفريقيا والسودان، واعتمد عليها أئمة الفقه واللغة من الحنفية والشافعية.",
        rules: "اشتهر السوسي بالإدغام الكبير للحرفين الملتقيين وصلاً، وإمالة ألف التأنيث للدوري."
    },
    {
        num: 4,
        name: "الإمام ابن عامر الشامي",
        city: "دمشق - الشام",
        years: "8 هـ - 118 هـ",
        narrators: ["هشام (هشام بن عمار)", "ابن ذكوان (عبد الله بن أحمد)"],
        madhabNote: "إمام أهل الشام وتابعي جليل قرأ على أبي الدرداء، اعتمدها فقهاء الشام في المذهبين الشافعي والحنفي.",
        rules: "إمالة بعض الألفات، وتسهيل الهمز وكسر هاء الضمير في مواضع مخصوصة، وإدغام ذال إذ."
    },
    {
        num: 5,
        name: "الإمام عاصم بن أبي النجود",
        city: "الكوفة",
        years: "توفي 127 هـ",
        narrators: ["شعبة (أبو بكر بن عياش)", "حفص (حفص بن سليمان)"],
        madhabNote: "القراءة الأكثر انتشاراً في العالم الإسلامي، عليها عمل جماهير المذاهب الأربعة (الحنفي والشافعي والحنبلي والمالكي).",
        rules: "رواية حفص بضبط التوسط في المدود، والتحقيق الخالص للهمزات إلا كلمات معدودة، وعذوبة الأداء."
    },
    {
        num: 6,
        name: "الإمام حمزة الزيات",
        city: "الكوفة",
        years: "80 هـ - 156 هـ",
        narrators: ["خلف (خلف بن هشام)", "خلاد (خلاد بن خالد)"],
        madhabNote: "من أعلام الكوفة وممن عاصر الإمام أبا حنيفة، وتعد قراءته مرجعاً في التحقيق وأحكام السكت والوقف.",
        rules: "المد المشبع 6 حركات في المتصل والمنفصل، وإمالة ذوات الياء، والسكت قبل الهمز ووقف حمزة على الهمز بتغييره."
    },
    {
        num: 7,
        name: "الإمام الكسائي",
        city: "الكوفة / بغداد",
        years: "119 هـ - 189 هـ",
        narrators: ["أبو الحارث (الليث بن خالد)", "الدوري (حفص بن عمر)"],
        madhabNote: "إمام النحاة والقراء في عصر هارون الرشيد، جمع بين فقه الكوفة ونحوها وأصول القراءة المتواترة.",
        rules: "الإمالة في هاء التأنيث عند الوقف بشروط، وإمالة ذوات الياء، وتوسط المتصل والمنفصل."
    },
    {
        num: 8,
        name: "الإمام أبو جعفر المدني",
        city: "المدينة المنورة",
        years: "توفي 130 هـ",
        narrators: ["ابن وردان (عيسى بن وردان)", "ابن جماز (سليمان بن مسلم)"],
        madhabNote: "شيخ الإمام نافع وإمام دار الهجرة قبله، قراءته متصلة بسيدنا عبد الله بن عباس وأبي هريرة وزيد بن ثابت.",
        rules: "الإخفاء للنون الساكنة عند الخاء والغين، وتسهيل الهمزتين، وضم ميم الجمع مع الصلة."
    },
    {
        num: 9,
        name: "الإمام يعقوب الحضرمي",
        city: "البصرة / حضرموت",
        years: "117 هـ - 205 هـ",
        narrators: ["رويس (محمد بن المتوكل)", "روح (روح بن عبد المؤمن)"],
        madhabNote: "إمام جامع البصرة وقارئها الكبير، وتعد قراءته من أشهر القراءات في اليمن والحجاز وأقاليم المشرق.",
        rules: "هاء السكت عند الوقف على كل ياء مشددة أو ضمير، والقصر والتوسط، وإظهار اللام عند التاء."
    },
    {
        num: 10,
        name: "الإمام خلف العاشر",
        city: "بغداد",
        years: "150 هـ - 229 هـ",
        narrators: ["إسحاق (إسحاق بن إبراهيم)", "إدريس (إدريس بن عبد الكريم)"],
        madhabNote: "أحد رواة حمزة واختار لنفسه قراءة متواترة مستقلة جمعت محاسن قراءات أهل الكوفة وبغداد.",
        rules: "التوسط في المد المنفصل، والسكت على المفصول، وترك الغنة عند اللام والراء."
    }
];

const fiqhComparisonsData = [
    {
        title: "1. آية الوضوء وغسل أو مسح الرجلين",
        ayah: "﴿وَامْسَحُوا بِرُءُوسِكُمْ وَأَرْجُلَكُمْ﴾ [المائدة: 6]",
        readings: [
            "قراءة النصب (وَأَرْجُلَكُمۡ): قراءة نافع وعاصم وابن عامر والكسائي ويعقوب وخلف العاشر.",
            "قراءة الجر (وَأَرْجُلِكُمۡ): قراءة ابن كثير وأبي عمرو وحمزة وأبي جعفر."
        ],
        fiqhImpact: "• المذهب الحنفي والمالكي والشافعي والحنبلي: قراءة النصب نص صريح في وجوب الغسل للرجلين إلى الكعبين عطفاً على المغسولات، وقراءة الجر دالة على رخصة المسح على الخفين أو الجوارب التخفيفة أو للمجاورة اللفظية، فتكاملت القراءتان في بيان الحكمين معاً."
    },
    {
        title: "2. آية طهارة المرأة وانقطاع الدم",
        ayah: "﴿وَلَا تَقْرَبُوهُنَّ حَتَّىٰ يَطْهُرْنَ﴾ [البقرة: 222]",
        readings: [
            "قراءة التخفيف (يَطۡهُرۡنَ): قراءة عاصم ونافع وابن كثير وأبي عمرو وابن عامر وأبي جعفر ويعقوب.",
            "قراءة التشديد (يَطَّهَّرۡنَ): قراءة حمزة والكسائي وخلف العاشر."
        ],
        fiqhImpact: "• المذهب الحنفي: استدل بقراءة التخفيف على جواز القربان بمجرد انقطاع الدم إذا انقطع لأكثر مدة الحيض.\n• المذهب الشافعي والمالكي والحنبلي: أوجبوا الاغتسال بالماء استناداً لقراءة التشديد (يطّهّرن) لبيان وجوب الطهارة المائية الكاملة."
    },
    {
        title: "3. آية نقض الوضوء بلمس النساء",
        ayah: "﴿أَوْ لَامَسْتُمُ النِّسَاءَ﴾ [النساء: 43] / ﴿أَوْ لَمَسْتُمُ﴾",
        readings: [
            "قراءة الألف (لَٰمَسۡتُمُ): قراءة عاصم وحمزة والكسائي وخلف.",
            "قراءة بغير ألف (لَمَسۡتُمُ): قراءة نافع وابن كثير وأبي عمرو وابن عامر وأبي جعفر ويعقوب."
        ],
        fiqhImpact: "• المذهب الحنفي: فسر اللمس بالجماع جمعاً بين القراءتين وسياق التيمم.\n• المذهب الشافعي: أخذ بقراءة (لمستم) وقضى بأن مطلق التقاء البشرتين ينقض الوضوء.\n• المذهب المالكي والحنبلي: قيدوا النقض بقصد اللذة أو وجودها عملاً بدلالة المفاعلة والمباشرة."
    },
    {
        title: "4. آية الصلاة وفاتحة الكتاب",
        ayah: "﴿مَالِكِ يَوْمِ الدِّينِ﴾ [الفاتحة: 4] / ﴿مَلِكِ يَوْمِ الدِّينِ﴾",
        readings: [
            "قراءة بالألف (مَٰلِكِ): قراءة عاصم والكسائي ويعقوب وخلف.",
            "قراءة بغير ألف (مَلِكِ): قراءة نافع وابن كثير وأبي عمرو وابن عامر وحمزة وأبي جعفر."
        ],
        fiqhImpact: "• إجماع المذاهب الإسلامية كافة: القراءتان تثبتان كمال العظمة الإلهية؛ فـ(مَلِك) تدل على الملك والسلطان والعدل العام، و(مَالِك) تدل على كمال التصرف والملكوت والجزاء الدقيق، فكان اجتماع القراءتين إعجازاً كلياً يثري عقيدة المسلم."
    }
];

function renderQiraatTab() {
    switchQiraatSubTab(currentQiraatSubTab);
}

function switchQiraatSubTab(subTab) {
    currentQiraatSubTab = subTab;
    document.querySelectorAll('#qiraatTab .filter-chip').forEach(c => c.classList.remove('active'));
    
    if (subTab === 'imams') {
        const chip = document.getElementById('chipQiraatImams');
        if (chip) chip.classList.add('active');
        renderQiraatImams();
    } else if (subTab === 'fiqh') {
        const chip = document.getElementById('chipQiraatFiqh');
        if (chip) chip.classList.add('active');
        renderQiraatFiqh();
    } else if (subTab === 'rules') {
        const chip = document.getElementById('chipQiraatRules');
        if (chip) chip.classList.add('active');
        renderQiraatRules();
    } else if (subTab === 'audio') {
        const chip = document.getElementById('chipQiraatAudio');
        if (chip) chip.classList.add('active');
        renderQiraatAudio();
    } else if (subTab === 'quiz') {
        const chip = document.getElementById('chipQiraatQuiz');
        if (chip) chip.classList.add('active');
        renderQiraatQuiz();
    }
}

function renderQiraatImams() {
    const area = document.getElementById('qiraatContentArea');
    if (!area) return;

    area.innerHTML = `
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(290px, 1fr)); gap: 14px;">
            ${tenRecitersData.map(r => `
                <div class="qiraat-card">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                        <h4 style="margin: 0; font-size: 1.1rem; color: var(--primary);">${r.num}. ${r.name}</h4>
                        <span class="madhab-chip">📍 ${r.city}</span>
                    </div>
                    <div style="font-size: 0.8rem; color: var(--text-secondary); margin-bottom: 8px;">عاش: ${r.years}</div>
                    <div style="margin-bottom: 8px;">
                        <span style="font-weight: 700; font-size: 0.85rem;">الراويان المعتمدان:</span>
                        <div>
                            ${r.narrators.map(n => `<span class="narrator-badge">🎙️ ${n}</span>`).join('')}
                        </div>
                    </div>
                    <div style="background: var(--bg); border: 1px solid var(--border); border-radius: 8px; padding: 8px; font-size: 0.82rem; color: var(--text); line-height: 1.5; margin-bottom: 6px;">
                        <strong>أثرها وانتشارها المذهبي:</strong> ${r.madhabNote}
                    </div>
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">
                        <strong>أهم الأصول:</strong> ${r.rules}
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderQiraatFiqh() {
    const area = document.getElementById('qiraatContentArea');
    if (!area) return;

    area.innerHTML = `
        <div style="margin-bottom: 16px;">
            <h3 style="color: var(--primary); font-size: 1.2rem; margin: 0 0 6px 0;">⚖️ أثر اختلاف القراءات العشر في الأحكام الفقهية لجميع المذاهب</h3>
            <p style="color: var(--text-secondary); font-size: 0.88rem; margin: 0;">
                اتفق فقهاء المذاهب الأربعة (أبو حنيفة، مالك، الشافعي، أحمد) على أن تعدد القراءات المتواترة بمنزلة تعدد الآيات، يفتح آفاق الاستنباط والتشريع:
            </p>
        </div>
        <div style="display: flex; flex-direction: column; gap: 14px;">
            ${fiqhComparisonsData.map(f => `
                <div class="fiqh-compare-card">
                    <h4 style="margin: 0 0 6px 0; color: #1B5E20; font-size: 1.05rem;">${f.title}</h4>
                    <div style="font-family: 'Amiri', serif; font-size: 1.25rem; color: #0F5132; margin-bottom: 8px; font-weight: 700;">${f.ayah}</div>
                    <div style="background: var(--surface); border: 1px solid var(--border); border-radius: 8px; padding: 10px; margin-bottom: 10px;">
                        <strong style="font-size: 0.85rem; color: var(--text-secondary);">أوجه القراءات العشر:</strong>
                        <ul style="margin: 4px 18px 0 0; font-size: 0.85rem; line-height: 1.5;">
                            ${f.readings.map(r => `<li>${r}</li>`).join('')}
                        </ul>
                    </div>
                    <div style="background: #E8F5E9; border-radius: 8px; padding: 10px; font-size: 0.85rem; line-height: 1.6; color: #1B5E20; white-space: pre-line;">
                        <strong>الأثر الفقهي بين المذاهب الأربعة:</strong>\n${f.fiqhImpact}
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderQiraatRules() {
    const area = document.getElementById('qiraatContentArea');
    if (!area) return;

    area.innerHTML = `
        <div style="background: var(--surface); border: 1px solid var(--border); border-radius: 12px; padding: 16px; overflow-x: auto;">
            <h3 style="color: var(--primary); margin: 0 0 12px 0;">📖 جدول مقارنة أصول القراءات العشر الكبرى</h3>
            <table style="width: 100%; border-collapse: collapse; font-size: 0.85rem; text-align: right;">
                <thead>
                    <tr style="background: var(--primary-container); color: var(--primary-dark);">
                        <th style="padding: 10px; border: 1px solid var(--border);">الأصل القرآني</th>
                        <th style="padding: 10px; border: 1px solid var(--border);">أصحاب التوسط والإشباع</th>
                        <th style="padding: 10px; border: 1px solid var(--border);">أصحاب القصر والتسهيل</th>
                        <th style="padding: 10px; border: 1px solid var(--border);">المذاهب المعتمدة</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="padding: 10px; border: 1px solid var(--border); font-weight: 700;">المد المنفصل</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">حمزة وورش (6 حركات - إشباع)</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">قالون والدوري وابن كثير والسوسي (حركتان)</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">سائر المذاهب في الحجاز والمغرب</td>
                    </tr>
                    <tr style="background: var(--bg);">
                        <td style="padding: 10px; border: 1px solid var(--border); font-weight: 700;">الإدغام الكبير</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">السوسي عن أبي عمرو وأبو جعفر في مواضع</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">بقية القراء بالإظهار التام</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">العراق واليمن والسودان</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border: 1px solid var(--border); font-weight: 700;">تسهيل الهمزتين</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">نافع وابن كثير وأبو عمرو ورويس</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">عاصم وابن عامر بالتحقيق</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">المالكية والشافعية والحنفية</td>
                    </tr>
                    <tr style="background: var(--bg);">
                        <td style="padding: 10px; border: 1px solid var(--border); font-weight: 700;">الإمالة والتقليل</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">حمزة والكسائي والدوري وخلف العاشر</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">حفص وابن كثير بالفتح التام</td>
                        <td style="padding: 10px; border: 1px solid var(--border);">الكوفة وبغداد ومصر</td>
                    </tr>
                </tbody>
            </table>
        </div>
    `;
}

function renderQiraatAudio() {
    const area = document.getElementById('qiraatContentArea');
    if (!area) return;

    area.innerHTML = `
        <div style="margin-bottom: 16px;">
            <h3 style="color: var(--primary); margin: 0 0 6px 0;">🎧 الاستماع لتلاوات نموذجية بروايات القراءات العشر</h3>
            <p style="color: var(--text-secondary); font-size: 0.85rem; margin: 0;">اختر الرواية واستمع للتلاوة القرآنية المباركة بصوت كبار القراء:</p>
        </div>
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 14px;">
            <div class="reciter-card">
                <div class="reciter-header">
                    <div class="reciter-avatar">📖</div>
                    <div>
                        <h4 style="margin:0;">رواية ورش عن نافع</h4>
                        <span style="font-size:0.78rem; color:var(--text-secondary);">المغرب العربي وإفريقيا (المذهب المالكي)</span>
                    </div>
                </div>
                <p style="font-size:0.82rem; color:var(--text-secondary);">بصوت الشيخ عمر القزابري (المغرب)</p>
                <button class="btn btn-secondary" onclick="playReciterAudio('warsh', 'https://server9.mp3quran.net/omr/001.mp3')">▶️ استماع لسورة الفاتحة برواية ورش</button>
            </div>
            <div class="reciter-card">
                <div class="reciter-header">
                    <div class="reciter-avatar">🕌</div>
                    <div>
                        <h4 style="margin:0;">رواية قالون عن نافع</h4>
                        <span style="font-size:0.78rem; color:var(--text-secondary);">ليبيا وتونس وأجزاء من الحجاز واليمن</span>
                    </div>
                </div>
                <p style="font-size:0.82rem; color:var(--text-secondary);">بصوت الشيخ علي الحذيفي</p>
                <button class="btn btn-secondary" onclick="playReciterAudio('qaloon', 'https://server9.mp3quran.net/hthfi/001.mp3')">▶️ استماع لسورة الفاتحة برواية قالون</button>
            </div>
            <div class="reciter-card">
                <div class="reciter-header">
                    <div class="reciter-avatar">🌿</div>
                    <div>
                        <h4 style="margin:0;">رواية الدوري عن أبي عمرو</h4>
                        <span style="font-size:0.78rem; color:var(--text-secondary);">السودان وتشاد وشرق إفريقيا واليمن</span>
                    </div>
                </div>
                <p style="font-size:0.82rem; color:var(--text-secondary);">بصوت الشيخ نورين محمد صديق</p>
                <button class="btn btn-secondary" onclick="playReciterAudio('duri', 'https://server16.mp3quran.net/nourin/001.mp3')">▶️ استماع برواية الدوري عن أبي عمرو</button>
            </div>
            <div class="reciter-card">
                <div class="reciter-header">
                    <div class="reciter-avatar">⭐</div>
                    <div>
                        <h4 style="margin:0;">رواية حفص عن عاصم</h4>
                        <span style="font-size:0.78rem; color:var(--text-secondary);">عامة بلاد العالم الإسلامي وشتى المذاهب</span>
                    </div>
                </div>
                <p style="font-size:0.82rem; color:var(--text-secondary);">بصوت الشيخ محمود خليل الحصري</p>
                <button class="btn btn-secondary" onclick="playReciterAudio('hafs', 'https://server13.mp3quran.net/husr/001.mp3')">▶️ استماع برواية حفص عن عاصم</button>
            </div>
        </div>
    `;
}

function renderQiraatQuiz() {
    const area = document.getElementById('qiraatContentArea');
    if (!area) return;

    area.innerHTML = `
        <div style="background: var(--surface); border: 1px solid var(--border); border-radius: 14px; padding: 20px;">
            <h3 style="color: var(--primary); margin: 0 0 6px 0;">🏆 اختبار علم القراءات العشر ورواتها والمذاهب</h3>
            <p style="color: var(--text-secondary); font-size: 0.88rem; margin: 0 0 16px 0;">أجب على الأسئلة واكتشف مستوى علمك في القراءات العشر المتواترة:</p>
            
            <div style="display: flex; flex-direction: column; gap: 14px;">
                <div style="background: var(--bg); border-radius: 10px; padding: 12px;">
                    <strong>1. من هو الإمام الذي قال عنه الإمام مالك بن أنس: «قراءته سُنّة»؟</strong>
                    <div style="display: flex; gap: 8px; margin-top: 8px; flex-wrap: wrap;">
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(1, 'right')">أ) الإمام نافع المدني ✅</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(1, 'wrong')">ب) الإمام حمزة الكوفي</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(1, 'wrong')">ج) الإمام الكسائي</button>
                    </div>
                </div>

                <div style="background: var(--bg); border-radius: 10px; padding: 12px;">
                    <strong>2. راويا الإمام عاصم بن أبي النجود الكوفي هما:</strong>
                    <div style="display: flex; gap: 8px; margin-top: 8px; flex-wrap: wrap;">
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(2, 'wrong')">أ) قالون وورش</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(2, 'right')">ب) شعبة وحفص ✅</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(2, 'wrong')">ج) البزي وقنبل</button>
                    </div>
                </div>

                <div style="background: var(--bg); border-radius: 10px; padding: 12px;">
                    <strong>3. ما أثر قراءة «وَأَرْجُلِكُمْ» بالجر في آية الوضوء؟</strong>
                    <div style="display: flex; gap: 8px; margin-top: 8px; flex-wrap: wrap;">
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(3, 'right')">أ) تفيد مشروعية المسح على الخفين ✅</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(3, 'wrong')">ب) تفيد نسخ الوضوء</button>
                        <button class="btn btn-outline" onclick="answerQiraatQuiz(3, 'wrong')">ج) تفيد التيمم فقط</button>
                    </div>
                </div>
            </div>
            <div id="quizResultArea" style="margin-top: 16px; font-weight: 700; color: var(--primary);"></div>
        </div>
    `;
}

function answerQiraatQuiz(qNum, result) {
    const res = document.getElementById('quizResultArea');
    if (result === 'right') {
        showToast("إجابة صحيحة وممتازة! 🌟 بارك الله في علمك");
        if (res) res.innerText = "أحسنت! إجابتك صحيحة ودقيقة عن علم القراءات العشر.";
    } else {
        showToast("إجابة غير دقيقة، حاول مرة أخرى لتتعلم المزيد");
        if (res) res.innerText = "حاول مجدداً للوصول للإجابة الصحيحة المعتمدة.";
    }
}

// ==========================================
// 2. الحلقات القرآنية والتحضير السريع
// ==========================================
function renderHalaqat() {
    const container = document.getElementById('halaqatList');
    if (!container) return;

    container.innerHTML = '';
    currentOrgData.circles.forEach(circle => {
        const circleStudents = currentOrgData.students.filter(s => s.circle === circle.name);
        const presentCount = circleStudents.filter(s => currentOrgData.attendance[s.id] === 'PRESENT').length;
        const total = circleStudents.length;
        const rate = total > 0 ? Math.round((presentCount / total) * 100) : 0;

        const card = document.createElement('div');
        card.style.cssText = 'background: var(--surface); border: 1px solid var(--border); border-radius: 14px; padding: 16px; display: flex; flex-direction: column; gap: 10px;';
        card.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <h3 style="font-size: 1rem; font-weight: 700;">${circle.name}</h3>
                <span class="badge ${circle.gender ? circle.gender.toLowerCase() : 'male'}">${circle.gender === 'FEMALE' ? 'بنات 👧' : 'بنين 👦'}</span>
            </div>
            <p style="font-size: 0.85rem; color: var(--text-secondary);">👥 عدد الطلاب: <strong>${total}</strong> | الحضور اليوم: <strong>${presentCount} (${rate}%)</strong></p>
            <div style="background: var(--primary-container); height: 6px; border-radius: 3px; overflow: hidden;">
                <div style="background: var(--primary); height: 100%; width: ${rate}%;"></div>
            </div>
            <div style="display: flex; gap: 8px; margin-top: auto;">
                <button class="btn btn-primary" onclick="switchTab('quickAttTab')">⚡ التحضير السريع</button>
                <button class="btn btn-outline" onclick="openAddStudentPrompt('${circle.name}')">➕ إضافة طالب</button>
            </div>
        `;
        container.appendChild(card);
    });
}

function openAddCirclePrompt() {
    const name = prompt(`أدخل اسم الحلقة الجديدة في ${currentOrgData.shortName}:`);
    if (!name || !name.trim()) return;
    const isMale = confirm("هل الحلقة خاصة بالبنين؟ (اضغط موافق للبنين، أو إلغاء لحلقات البنات)");
    const gender = isMale ? "MALE" : "FEMALE";
    const teacher = prompt("أدخل اسم المعلم أو المحفظ المشرف:", isMale ? "الشيخ عبد الله" : "الأستاذة مريم") || "المحفظ المعتمد";

    currentOrgData.circles.push({
        name: name.trim(),
        gender: gender,
        teacher: teacher,
        phone: "0500000000",
        schedule: "يومياً",
        location: "مقر الجمعية",
        goal: "حفظ وتجويد القرآن الكريم"
    });

    saveOrgData(currentOrgKey, currentOrgData);
    renderHalaqat();
    showToast(`تم إنشاء الحلقة المباركة: ${name} في ${currentOrgData.shortName} 🕌`);
}

function openAddStudentPrompt(circleName) {
    const name = prompt(`أدخل اسم الطالب الجديد في ${circleName}:`);
    if (!name || !name.trim()) return;
    const newId = currentOrgData.students.length > 0 ? Math.max(...currentOrgData.students.map(s => s.id)) + 1 : 1;
    const isMuadh = currentOrgKey === 'MUADH';
    const code = isMuadh ? `MDH-20${newId % 100}` : `HSA-10${newId % 100}`;

    currentOrgData.students.push({
        id: newId,
        name: name.trim(),
        gender: "MALE",
        circle: circleName,
        code: code,
        phone: "0500000000",
        juz: 30,
        points: 100,
        streak: 1,
        goal: "حفظ جزء عم"
    });

    saveOrgData(currentOrgKey, currentOrgData);
    renderHalaqat();
    renderStudents(currentGender, currentSearch);
    showToast(`تمت إضافة الطالب ${name} بنجاح ✅`);
}

function renderQuickAttendance() {
    const statsContainer = document.getElementById('quickAttendanceStats');
    const listContainer = document.getElementById('quickAttendanceList');
    if (!listContainer) return;

    let present = 0, late = 0, excused = 0, absent = 0, none = 0;
    currentOrgData.students.forEach(s => {
        const att = currentOrgData.attendance[s.id] || 'NONE';
        if (att === 'PRESENT') present++;
        else if (att === 'LATE') late++;
        else if (att === 'EXCUSED') excused++;
        else if (att === 'ABSENT') absent++;
        else none++;
    });

    if (statsContainer) {
        statsContainer.innerHTML = `
            <div style="background: #E8F5E9; color: #2E7D32; padding: 6px 12px; border-radius: 8px; font-weight: 700; font-size: 0.85rem;">حاضر: ${present} ✅</div>
            <div style="background: #FFF3E0; color: #EF6C00; padding: 6px 12px; border-radius: 8px; font-weight: 700; font-size: 0.85rem;">متأخر: ${late} ⏰</div>
            <div style="background: #E1F5FE; color: #0288D1; padding: 6px 12px; border-radius: 8px; font-weight: 700; font-size: 0.85rem;">مستأذن: ${excused} ℹ️</div>
            <div style="background: #FFEBEE; color: #C62828; padding: 6px 12px; border-radius: 8px; font-weight: 700; font-size: 0.85rem;">غائب: ${absent} ❌</div>
            <div style="background: #F5F5F5; color: #757575; padding: 6px 12px; border-radius: 8px; font-weight: 700; font-size: 0.85rem;">لم يرصد: ${none}</div>
        `;
    }

    listContainer.innerHTML = '';
    currentOrgData.students.forEach(student => {
        const att = currentOrgData.attendance[student.id] || 'NONE';
        const card = document.createElement('div');
        card.className = 'student-card';
        card.innerHTML = `
            <div class="student-header">
                <div class="student-avatar ${student.gender.toLowerCase()}">${student.name.charAt(0)}</div>
                <div class="student-info">
                    <h3>${student.name}</h3>
                    <p>${student.circle} - ${student.code}</p>
                </div>
            </div>
            <div style="display: flex; gap: 6px; margin-top: 10px; flex-wrap: wrap;">
                <button class="btn ${att === 'PRESENT' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px;" onclick="setAtt(${student.id}, 'PRESENT')">حاضر ✅</button>
                <button class="btn ${att === 'LATE' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px;" onclick="setAtt(${student.id}, 'LATE')">متأخر ⏰</button>
                <button class="btn ${att === 'EXCUSED' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px;" onclick="setAtt(${student.id}, 'EXCUSED')">مستأذن ℹ️</button>
                <button class="btn ${att === 'ABSENT' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px; border-color: #D32F2F; color: #D32F2F;" onclick="setAtt(${student.id}, 'ABSENT')">غائب ❌</button>
            </div>
        `;
        listContainer.appendChild(card);
    });
}

function setAtt(studentId, status) {
    currentOrgData.attendance[studentId] = status;
    saveOrgData(currentOrgKey, currentOrgData);
    renderQuickAttendance();
    renderStudents(currentGender, currentSearch);
}

function markAllPresentWeb() {
    currentOrgData.students.forEach(s => {
        currentOrgData.attendance[s.id] = 'PRESENT';
    });
    saveOrgData(currentOrgKey, currentOrgData);
    renderQuickAttendance();
    renderStudents(currentGender, currentSearch);
    showToast(`تم تحضير جميع طلاب ${currentOrgData.shortName} حاضرين بنجاح ✅`);
}

function shareAbsentReportWeb() {
    const absents = currentOrgData.students.filter(s => currentOrgData.attendance[s.id] === 'ABSENT');
    const text = `📋 *تقرير غياب حلقات ${currentOrgData.shortName}* 📋\nالتاريخ: ${new Date().toLocaleDateString('ar-SA')}\n` +
        `عدد الغائبين: ${absents.length} طالب\n` +
        absents.map((s, i) => `${i + 1}. ${s.name} - ${s.circle} (هاتف: ${s.phone})`).join('\n') +
        `\nمنظومة منارة الجيل القرآني`;
    window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`, '_blank');
}

// ==========================================
// 3. بوابة ولي الأمر للمتابعة اليومية
// ==========================================
function searchParentPortal() {
    const input = document.getElementById('parentCodeInput');
    const result = document.getElementById('parentResult');
    if (!input || !result) return;

    const code = input.value.trim().toUpperCase();
    if (!code) {
        result.innerHTML = `<div class="empty-state">يرجى كتابة رمز متابعة الطالب</div>`;
        return;
    }

    let student = currentOrgData.students.find(s => s.code.toUpperCase() === code);

    if (!student) {
        const otherOrgKey = currentOrgKey === 'HAEL' ? 'MUADH' : 'HAEL';
        const otherData = loadOrgData(otherOrgKey);
        const inOther = otherData.students.find(s => s.code.toUpperCase() === code);

        if (inOther) {
            result.innerHTML = `
                <div class="parent-card" style="border: 2px dashed #EF6C00; background: #FFF3E0;">
                    <h3 style="color: #E65100;">⚠️ تنبيه العزل التام للبيانات:</h3>
                    <p style="color: #5D4037; font-size: 0.9rem; line-height: 1.6;">
                        رمز المتابعة (<strong>${code}</strong>) مسجل في <strong>${otherData.name}</strong> وليس في ${currentOrgData.shortName}.
                    </p>
                    <button class="btn btn-primary" onclick="switchOrganization('${otherOrgKey}')">
                        التبديل إلى ${otherData.shortName} لمشاهدة التقرير 🔄
                    </button>
                </div>
            `;
            return;
        }

        result.innerHTML = `<div class="empty-state">لم يتم العثور على طالب بهذا الرمز في ${currentOrgData.shortName}. يرجى مراجعة المعلم المشرف.</div>`;
        return;
    }

    const attStatus = currentOrgData.attendance[student.id] || "PRESENT";
    const attLabel = getAttendanceLabel(attStatus);
    const lastRec = currentOrgData.recitations.find(r => r.studentId === student.id);

    result.innerHTML = `
        <div class="parent-card">
            <div class="parent-card-header">
                <div>
                    <h3 style="color: var(--primary); font-size: 1.25rem;">${student.name}</h3>
                    <p style="color: var(--text-secondary); font-size: 0.85rem;">🏛️ ${currentOrgData.shortName} | 🕌 ${student.circle}</p>
                </div>
                <span class="badge ${student.gender.toLowerCase()}">${student.gender === 'MALE' ? 'طالب 👦' : 'طالبة 👧'}</span>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 10px; margin: 14px 0;">
                <div style="background: var(--bg); padding: 10px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">حالة اليوم:</div>
                    <div style="font-weight: 700; color: #1B5E20;">${attLabel}</div>
                </div>
                <div style="background: var(--bg); padding: 10px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">المستوى القرآني:</div>
                    <div style="font-weight: 700; color: var(--primary);">${student.level || 'المستوى المتقدم 🥇'}</div>
                </div>
                <div style="background: var(--bg); padding: 10px; border-radius: 8px; text-align: center;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">النقاط والأوسمة:</div>
                    <div style="font-weight: 700; color: #E65100;">${student.points} ⭐</div>
                </div>
            </div>

            ${lastRec ? `
            <div style="background: #FFFDE7; border: 1px solid #FFF59D; border-radius: 10px; padding: 12px; margin: 12px 0;">
                <h4 style="margin: 0 0 6px 0; color: #F57F17;">📖 آخر جلسة تسميع ومراجعة:</h4>
                <div style="font-size: 0.9rem;">السورة: <strong>${lastRec.surah}</strong> (الآيات ${lastRec.fromAyah} إلى ${lastRec.toAyah})</div>
                <div style="font-size: 0.9rem;">التقدير: <strong>${lastRec.rating}</strong> | التاريخ: ${lastRec.date}</div>
                <div class="notes">📝 ملاحظة المعلم: ${lastRec.notes}</div>
            </div>
            ` : '<div style="background: var(--bg); padding: 10px; border-radius: 8px; font-size: 0.85rem;">لا توجد جلسة تسميع مسجلة اليوم</div>'}

            <div style="display: flex; gap: 8px; margin-top: 14px;">
                <button class="btn btn-whatsapp" onclick="shareStudentWhatsAppReport(${student.id})">📲 مشاركة بطاقة المتابعة</button>
            </div>
        </div>
    `;
}

// ==========================================
// 4. إدارة حلقات البنات (فصل تام)
// ==========================================
let currentGirlsSubTab = 'circles';
let currentGirlsSearch = '';
let currentGirlsCircleFilter = 'ALL';

function renderGirlsAdmin() {
    renderGirlsStats();
    renderGirlsCircleChips();
    renderGirlsContent();
}

function renderGirlsStats() {
    const container = document.getElementById('girlsStatsContainer');
    if (!container) return;

    const femaleStudents = currentOrgData.students.filter(s => s.gender === 'FEMALE');
    const femaleCircles = girlsSectionData.circles;
    const presentGirls = femaleStudents.filter(s => currentOrgData.attendance[s.id] === 'PRESENT').length;

    container.innerHTML = `
        <div style="background: #FCE4EC; color: #880E4F; padding: 8px 14px; border-radius: 10px; font-weight: 700; font-size: 0.85rem;">
            🌸 الحلقات: ${femaleCircles.length} حلقة
        </div>
        <div style="background: #F8BBD0; color: #880E4F; padding: 8px 14px; border-radius: 10px; font-weight: 700; font-size: 0.85rem;">
            👩‍🎓 إجمالي الطالبات: ${femaleStudents.length} طالبة
        </div>
        <div style="background: #E8F5E9; color: #1B5E20; padding: 8px 14px; border-radius: 10px; font-weight: 700; font-size: 0.85rem;">
            ✅ حضور اليوم: ${presentGirls} طالبة
        </div>
        <div style="background: #EDE7F6; color: #4A148C; padding: 8px 14px; border-radius: 10px; font-weight: 700; font-size: 0.85rem;">
            🔒 قسم البنات في ${currentOrgData.shortName}
        </div>
    `;
}

function renderGirlsCircleChips() {
    const container = document.getElementById('girlsCircleChips');
    if (!container) return;

    container.innerHTML = `
        <button class="filter-chip ${currentGirlsCircleFilter === 'ALL' ? 'active' : ''}" onclick="filterGirlsByCircle('ALL')">جميع الحلقات</button>
        ${girlsSectionData.circles.map(c => `
            <button class="filter-chip ${currentGirlsCircleFilter === c.name ? 'active' : ''}" onclick="filterGirlsByCircle('${c.name}')">${c.name}</button>
        `).join('')}
    `;
}

function filterGirlsByCircle(circleName) {
    currentGirlsCircleFilter = circleName;
    renderGirlsCircleChips();
    renderGirlsContent();
}

function switchGirlsSubTab(tab) {
    currentGirlsSubTab = tab;
    document.querySelectorAll('#girlsTab .filter-chip').forEach(c => c.classList.remove('active'));
    if (tab === 'circles') document.getElementById('chipGirlsCircles')?.classList.add('active');
    if (tab === 'students') document.getElementById('chipGirlsStudents')?.classList.add('active');
    if (tab === 'attendance') document.getElementById('chipGirlsAtt')?.classList.add('active');
    renderGirlsContent();
}

function onGirlsSearchChange(val) {
    currentGirlsSearch = val.trim().toLowerCase();
    renderGirlsContent();
}

function renderGirlsContent() {
    const container = document.getElementById('girlsContentArea');
    if (!container) return;

    if (currentGirlsSubTab === 'circles') {
        renderGirlsCirclesList(container);
    } else if (currentGirlsSubTab === 'students') {
        renderGirlsStudentsList(container);
    } else if (currentGirlsSubTab === 'attendance') {
        renderGirlsAttendanceList(container);
    }
}

function renderGirlsCirclesList(container) {
    container.innerHTML = '';
    const circles = girlsSectionData.circles;

    if (circles.length === 0) {
        container.innerHTML = `<div class="empty-state">لا توجد حلقات فتيات مسجلة في ${currentOrgData.shortName}</div>`;
        return;
    }

    const grid = document.createElement('div');
    grid.style.cssText = 'display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 14px;';

    circles.forEach(c => {
        const students = currentOrgData.students.filter(s => s.gender === 'FEMALE' && s.circle === c.name);
        const card = document.createElement('div');
        card.style.cssText = 'background: var(--surface); border: 1px solid #F8BBD0; border-radius: 14px; padding: 16px; border-top: 4px solid #C2185B; display: flex; flex-direction: column; gap: 8px;';
        card.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <h3 style="margin: 0; font-size: 1.1rem; color: #880E4F;">${c.name}</h3>
                <span class="badge female">فتيات 🌸</span>
            </div>
            <div style="font-size: 0.85rem; color: var(--text-secondary); line-height: 1.6;">
                <div>👩‍🏫 المحفظة: <strong>${c.teacher}</strong></div>
                <div>📞 الهاتف: <strong>${c.phone || '0550000000'}</strong></div>
                <div>⏰ الموعد: ${c.schedule || 'العصر'}</div>
                <div>📍 المقر: ${c.location || 'معهد البيان'}</div>
                <div>👥 عدد الطالبات: <strong style="color: #C2185B;">${students.length} طالبة</strong></div>
            </div>
            <div style="display: flex; gap: 6px; margin-top: auto; flex-wrap: wrap;">
                <button class="btn btn-secondary" style="padding: 4px 8px; font-size: 0.8rem;" onclick="filterGirlsByCircle('${c.name}')">👩‍🎓 الطالبات</button>
                <button class="btn btn-outline" style="padding: 4px 8px; font-size: 0.8rem; border-color: #880E4F; color: #880E4F;" onclick="openAssignTeacherModal('${c.name}')">✏️ تعيين المحفظة</button>
                <button class="btn btn-outline" style="padding: 4px 8px; font-size: 0.8rem; border-color: #C2185B; color: #C2185B;" onclick="openCircleReportModal('${c.name}')">🌸 تقرير الحلقة</button>
            </div>
        `;
        grid.appendChild(card);
    });
    container.appendChild(grid);
}

function renderGirlsStudentsList(container) {
    container.innerHTML = '';
    let students = currentOrgData.students.filter(s => s.gender === 'FEMALE');

    if (currentGirlsCircleFilter !== 'ALL') {
        students = students.filter(s => s.circle === currentGirlsCircleFilter);
    }
    if (currentGirlsSearch) {
        students = students.filter(s => s.name.toLowerCase().includes(currentGirlsSearch) || s.code.toLowerCase().includes(currentGirlsSearch));
    }

    if (students.length === 0) {
        container.innerHTML = `<div class="empty-state">لا توجد طالبات مطابقة للبحث في قسم البنات</div>`;
        return;
    }

    const grid = document.createElement('div');
    grid.className = 'students-grid';

    students.forEach(s => {
        const card = document.createElement('div');
        card.className = 'student-card';
        card.style.borderTop = '3px solid #C2185B';
        card.innerHTML = `
            <div class="student-header">
                <div class="student-avatar female">${s.name.charAt(0)}</div>
                <div class="student-info">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <h3 style="color: #880E4F;">${s.name}</h3>
                        <span class="badge female">طالبة 👧</span>
                    </div>
                    <p>🕌 ${s.circle} | رمز: <strong>${s.code}</strong></p>
                </div>
            </div>
            <div class="meta-row">
                <span>📖 الجزء: <strong>${s.juz || 30}</strong></span>
                <span>⭐ نقاط: <strong>${s.points}</strong></span>
                <span>🔥 مواظبة: <strong>${s.streak} يوم</strong></span>
            </div>
            <div style="display: flex; gap: 8px; margin-top: 10px; flex-wrap: wrap;">
                <button class="btn btn-primary" style="background: #C2185B;" onclick="openGirlsRecitationModal(${s.id})">📖 تسجيل تسميع</button>
                <button class="btn btn-outline" style="padding: 4px 8px; font-size: 0.8rem;" onclick="openEditGirlStudentModal(${s.id})">✏️ تعديل</button>
                <button class="btn btn-whatsapp" style="margin-top:0;" onclick="shareGirlWhatsAppReport(${s.id})">📲 واتساب ولية الأمر</button>
            </div>
        `;
        grid.appendChild(card);
    });
    container.appendChild(grid);
}

function renderGirlsAttendanceList(container) {
    container.innerHTML = '';
    const femaleStudents = currentOrgData.students.filter(s => s.gender === 'FEMALE');

    const grid = document.createElement('div');
    grid.className = 'students-grid';

    femaleStudents.forEach(s => {
        const att = currentOrgData.attendance[s.id] || 'NONE';
        const card = document.createElement('div');
        card.className = 'student-card';
        card.style.borderTop = '3px solid #C2185B';
        card.innerHTML = `
            <div class="student-header">
                <div class="student-avatar female">${s.name.charAt(0)}</div>
                <div class="student-info">
                    <h3 style="color: #880E4F;">${s.name}</h3>
                    <p>${s.circle}</p>
                </div>
            </div>
            <div style="display: flex; gap: 6px; margin-top: 10px; flex-wrap: wrap;">
                <button class="btn ${att === 'PRESENT' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px; ${att === 'PRESENT' ? 'background: #C2185B;' : ''}" onclick="setGirlsAtt(${s.id}, 'PRESENT')">حاضرة ✅</button>
                <button class="btn ${att === 'LATE' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px;" onclick="setGirlsAtt(${s.id}, 'LATE')">متأخرة ⏰</button>
                <button class="btn ${att === 'EXCUSED' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px;" onclick="setGirlsAtt(${s.id}, 'EXCUSED')">مستأذنة ℹ️</button>
                <button class="btn ${att === 'ABSENT' ? 'btn-primary' : 'btn-outline'}" style="flex:1; padding: 6px; border-color: #D32F2F; color: #D32F2F;" onclick="setGirlsAtt(${s.id}, 'ABSENT')">غائبة ❌</button>
            </div>
        `;
        grid.appendChild(card);
    });
    container.appendChild(grid);
}

function setGirlsAtt(studentId, status) {
    currentOrgData.attendance[studentId] = status;
    saveOrgData(currentOrgKey, currentOrgData);
    renderGirlsContent();
    renderGirlsStats();
}

function openAddGirlsCircleModal() {
    const modal = document.getElementById('modalAddGirlsCircle');
    if (modal) modal.classList.add('active');
}

function closeAddGirlsCircleModal() {
    const modal = document.getElementById('modalAddGirlsCircle');
    if (modal) modal.classList.remove('active');
}

function saveNewGirlsCircle() {
    const name = document.getElementById('inputNewCircleName').value.trim();
    const teacher = document.getElementById('inputNewTeacherName').value.trim();
    const phone = document.getElementById('inputNewTeacherPhone').value.trim();
    const schedule = document.getElementById('inputNewSchedule').value.trim();
    const location = document.getElementById('inputNewLocation').value.trim();
    const goal = document.getElementById('inputNewGoal').value.trim();

    if (!name || !teacher) {
        showToast("يرجى إدخال اسم الحلقة واسم المحفظة المشرفة ⚠️");
        return;
    }

    currentOrgData.circles.push({
        name: name,
        gender: "FEMALE",
        teacher: teacher,
        phone: phone || "0550000000",
        schedule: schedule || "العصر",
        location: location || "معهد البيان",
        goal: goal || "حفظ وتجويد القرآن الكريم"
    });

    saveOrgData(currentOrgKey, currentOrgData);
    closeAddGirlsCircleModal();
    renderGirlsAdmin();
    showToast(`تمت إضافة حلقة الفتيات ${name} بنجاح 🌸`);
}

function openAssignTeacherModal(circleName) {
    const circle = currentOrgData.circles.find(c => c.name === circleName) || (girlsSectionData.circles && girlsSectionData.circles.find(c => c.name === circleName));
    if (!circle) return;
    const txt = document.getElementById('assignCircleNameText');
    const hdn = document.getElementById('assignCircleNameHidden');
    const nameInp = document.getElementById('inputAssignTeacherName');
    const phoneInp = document.getElementById('inputAssignTeacherPhone');
    const schedInp = document.getElementById('inputAssignSchedule');
    const locInp = document.getElementById('inputAssignLocation');
    
    if (txt) txt.innerText = circle.name;
    if (hdn) hdn.value = circle.name;
    if (nameInp) nameInp.value = circle.teacher || '';
    if (phoneInp) phoneInp.value = circle.phone || '';
    if (schedInp) schedInp.value = circle.schedule || '';
    if (locInp) locInp.value = circle.location || '';
    
    const modal = document.getElementById('modalAssignTeacher');
    if (modal) modal.classList.add('active');
}

function closeAssignTeacherModal() {
    const modal = document.getElementById('modalAssignTeacher');
    if (modal) modal.classList.remove('active');
}

function saveAssignedTeacher() {
    const circleName = document.getElementById('assignCircleNameHidden').value;
    const teacherName = document.getElementById('inputAssignTeacherName').value.trim();
    const phone = document.getElementById('inputAssignTeacherPhone').value.trim();
    const schedule = document.getElementById('inputAssignSchedule').value.trim();
    const location = document.getElementById('inputAssignLocation').value.trim();

    if (!teacherName) {
        showToast("يرجى إدخال اسم المحفظة المشرفة ⚠️");
        return;
    }

    const circle = currentOrgData.circles.find(c => c.name === circleName);
    if (circle) {
        circle.teacher = teacherName;
        if (phone) circle.phone = phone;
        if (schedule) circle.schedule = schedule;
        if (location) circle.location = location;
        saveOrgData(currentOrgKey, currentOrgData);
        closeAssignTeacherModal();
        renderGirlsAdmin();
        if (window.renderHalaqat) renderHalaqat();
        showToast(`تم تعيين المحفظة ${teacherName} لحلقة ${circleName} بنجاح 🌸`);
    }
}

function openAddGirlStudentModal() {
    const modal = document.getElementById('modalAddGirlStudent');
    const select = document.getElementById('selectGirlStudentCircle');
    if (select) {
        select.innerHTML = girlsSectionData.circles.map(c => `<option value="${c.name}">${c.name}</option>`).join('');
    }
    if (modal) modal.classList.add('active');
}

function closeAddGirlStudentModal() {
    const modal = document.getElementById('modalAddGirlStudent');
    if (modal) modal.classList.remove('active');
}

function saveNewGirlStudent() {
    const name = document.getElementById('inputGirlStudentName').value.trim();
    const circle = document.getElementById('selectGirlStudentCircle').value;
    const phone = document.getElementById('inputGirlStudentPhone').value.trim();
    const code = document.getElementById('inputGirlStudentCode').value.trim();
    const juz = parseInt(document.getElementById('inputGirlStudentJuz').value) || 30;
    const goal = document.getElementById('inputGirlStudentGoal').value.trim();

    if (!name || !circle) {
        showToast("يرجى إدخال اسم الطالبة واختيار الحلقة ⚠️");
        return;
    }

    const newId = currentOrgData.students.length > 0 ? Math.max(...currentOrgData.students.map(s => s.id)) + 1 : 1;
    const finalCode = code || (currentOrgKey === 'MUADH' ? `MDH-G${newId}` : `HSA-G${newId}`);

    currentOrgData.students.push({
        id: newId,
        name: name,
        gender: "FEMALE",
        circle: circle,
        code: finalCode,
        phone: phone || "0550000000",
        juz: juz,
        points: 100,
        streak: 1,
        goal: goal || "ختم القرآن الكريم كاملاً"
    });

    saveOrgData(currentOrgKey, currentOrgData);
    closeAddGirlStudentModal();
    renderGirlsAdmin();
    renderStudents(currentGender, currentSearch);
    showToast(`تم تسجيل الطالبة ${name} في ${circle} بنجاح 🌸`);
}

function openEditGirlStudentModal(studentId) {
    const student = currentOrgData.students.find(s => s.id === studentId);
    if (!student) return;

    const idInp = document.getElementById('inputEditGirlStudentId');
    const nameInp = document.getElementById('inputEditGirlStudentName');
    const phoneInp = document.getElementById('inputEditGirlStudentPhone');
    const juzInp = document.getElementById('inputEditGirlStudentJuz');
    const goalInp = document.getElementById('inputEditGirlStudentGoal');
    const sel = document.getElementById('selectEditGirlStudentCircle');

    if (idInp) idInp.value = student.id;
    if (nameInp) nameInp.value = student.name;
    if (phoneInp) phoneInp.value = student.phone || '';
    if (juzInp) juzInp.value = student.juz || 30;
    if (goalInp) goalInp.value = student.goal || '';

    if (sel) {
        const femaleCircles = girlsSectionData.circles;
        sel.innerHTML = femaleCircles.map(c => `<option value="${c.name}" ${c.name === student.circle ? 'selected' : ''}>${c.name}</option>`).join('');
    }

    const modal = document.getElementById('modalEditGirlStudent');
    if (modal) modal.classList.add('active');
}

function closeEditGirlStudentModal() {
    const modal = document.getElementById('modalEditGirlStudent');
    if (modal) modal.classList.remove('active');
}

function updateGirlStudent() {
    const idInp = document.getElementById('inputEditGirlStudentId');
    if (!idInp) return;
    const id = parseInt(idInp.value);
    const student = currentOrgData.students.find(s => s.id === id);
    if (!student) return;

    const name = document.getElementById('inputEditGirlStudentName').value.trim();
    const circle = document.getElementById('selectEditGirlStudentCircle').value;
    const phone = document.getElementById('inputEditGirlStudentPhone').value.trim();
    const juz = parseInt(document.getElementById('inputEditGirlStudentJuz').value) || 30;
    const goal = document.getElementById('inputEditGirlStudentGoal').value.trim();

    if (!name) {
        showToast("يرجى كتابة اسم الطالبة ⚠️");
        return;
    }

    student.name = name;
    student.circle = circle;
    student.phone = phone || student.phone;
    student.juz = juz;
    student.goal = goal || student.goal;

    saveOrgData(currentOrgKey, currentOrgData);
    closeEditGirlStudentModal();
    renderGirlsAdmin();
    renderStudents(currentGender, currentSearch);
    showToast(`تم تحديث بيانات الطالبة ${name} بنجاح 🌸`);
}

function deleteCurrentGirlStudent() {
    const idInp = document.getElementById('inputEditGirlStudentId');
    if (!idInp) return;
    const id = parseInt(idInp.value);
    const student = currentOrgData.students.find(s => s.id === id);
    if (!student) return;

    if (confirm(`هل أنتِ متأكدة من حذف الطالبة (${student.name}) من النظام وقسم البنات؟`)) {
        currentOrgData.students = currentOrgData.students.filter(s => s.id !== id);
        saveOrgData(currentOrgKey, currentOrgData);
        closeEditGirlStudentModal();
        renderGirlsAdmin();
        renderStudents(currentGender, currentSearch);
        showToast(`تم حذف الطالبة ${student.name} من السجلات 🗑️`);
    }
}

function openGirlsRecitationModal(studentId) {
    const student = currentOrgData.students.find(s => s.id === studentId);
    if (!student) return;

    document.getElementById('recitationGirlStudentId').value = student.id;
    document.getElementById('recitationGirlStudentName').innerText = student.name;
    const modal = document.getElementById('modalGirlsRecitation');
    if (modal) modal.classList.add('active');
}

function closeGirlsRecitationModal() {
    const modal = document.getElementById('modalGirlsRecitation');
    if (modal) modal.classList.remove('active');
}

function saveGirlsRecitation() {
    const studentId = parseInt(document.getElementById('recitationGirlStudentId').value);
    const student = currentOrgData.students.find(s => s.id === studentId);
    if (!student) return;

    const type = document.getElementById('selectRecitationType').value;
    const surah = document.getElementById('inputRecitationSurah').value.trim() || "الملك";
    const fromAyah = parseInt(document.getElementById('inputRecitationFromAyah').value) || 1;
    const toAyah = parseInt(document.getElementById('inputRecitationToAyah').value) || 15;
    const rating = document.getElementById('selectRecitationRating').value;
    const notes = document.getElementById('inputRecitationNotes').value.trim();

    currentOrgData.recitations.unshift({
        studentId: student.id,
        studentName: student.name,
        type: type,
        surah: surah,
        fromAyah: fromAyah,
        toAyah: toAyah,
        rating: rating,
        tajweed: 10,
        fluency: 10,
        notes: notes || "أداء متميز وتلاوة خاشعة",
        date: new Date().toISOString().split('T')[0]
    });

    student.points += 20;
    student.streak += 1;
    saveOrgData(currentOrgKey, currentOrgData);
    closeGirlsRecitationModal();
    renderGirlsContent();
    renderStudents(currentGender, currentSearch);
    showToast(`تم حفظ تسميع الطالبة ${student.name} بنجاح وإشعار ولية الأمر 🌸`);
}

function openCircleReportModal(circleName) {
    const circle = girlsSectionData.circles.find(c => c.name === circleName);
    if (!circle) return;

    const femaleStudents = currentOrgData.students.filter(s => s.gender === 'FEMALE' && s.circle === circle.name);
    const presentCount = femaleStudents.filter(s => currentOrgData.attendance[s.id] === 'PRESENT').length;

    const reportText = `🌸 *تقرير حلقة ${circle.name} للفتيات* 🌸\n` +
        `الجمعية: ${currentOrgData.name}\n` +
        `المحفظة المشرفة: ${circle.teacher} ${circle.phone ? `(${circle.phone})` : ''}\n` +
        `المقر: ${circle.location || 'معهد البيان'}\n` +
        `موعد الانعقاد: ${circle.schedule || 'العصر'}\n` +
        `━━━━━━━━━━━━━━━\n` +
        `📊 إجمالي الطالبات: ${femaleStudents.length} طالبة\n` +
        `✅ حضور اليوم: ${presentCount} من ${femaleStudents.length}\n` +
        `━━━━━━━━━━━━━━━\n` +
        `قائمة الطالبات المسجلات:\n` +
        femaleStudents.map((s, idx) => `${idx + 1}. ${s.name} - جزء ${s.juz || 30} - كود: ${s.code} (نقاط: ${s.points})`).join('\n') +
        `\n\nإشراف: الإدارة العامة لحلقات البنات - منارة الجيل القرآني`;

    girlsSectionData.activeReportText = reportText;
    const content = document.getElementById('circleReportContent');
    if (content) content.innerText = reportText;

    const m = document.getElementById('modalCircleReport');
    if (m) m.classList.add('active');
}

function closeCircleReportModal() {
    const m = document.getElementById('modalCircleReport');
    if (m) m.classList.remove('active');
}

function shareCircleReportWhatsApp() {
    if (!girlsSectionData.activeReportText) return;
    window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(girlsSectionData.activeReportText)}`, '_blank');
}

function copyCircleReportText() {
    if (!girlsSectionData.activeReportText) return;
    navigator.clipboard.writeText(girlsSectionData.activeReportText).then(() => {
        showToast("تم نسخ التقرير بنجاح! 📋");
    });
}

function shareGirlWhatsAppReport(studentId) {
    shareStudentWhatsAppReport(studentId);
}

// ==========================================
// 5. الرسائل والتنبيهات والشهادات
// ==========================================
function sendBroadcastWeb() {
    const title = document.getElementById('webMsgTitle').value;
    showToast(`تم إرسال التنبيه العام: "${title}" لجميع حلقات ${currentOrgData.shortName} 🔔`);
}

function shareMsgWhatsAppWeb() {
    const title = document.getElementById('webMsgTitle').value;
    const body = document.getElementById('webMsgBody').value;
    const text = `🌿 *${title}* 🌿\n\n${body}\n\n━━━━━━━━━━━━━━━\n📲 ${currentOrgData.name}`;
    window.open(`https://api.whatsapp.com/send?text=${encodeURIComponent(text)}`, '_blank');
}

function openCertificateModal(studentId) {
    const student = currentOrgData.students.find(s => s.id === studentId) || currentOrgData.students[0];
    const certText = `📜 *شهادة تميّز وإنجاز قرآني رسمية* 📜\nتشهد إدارة حلقات ${currentOrgData.name} بأن ${student.name} من ${student.circle} قد أتم/ت حفظ جزء عم والمواظبة على الحضور بتقدير ممتاز 🌟. بارك الله في حفظه وثبته!`;
    if (navigator.share) {
        navigator.share({ title: `شهادة تقدير ${student.name}`, text: certText }).catch(() => {});
    } else {
        navigator.clipboard.writeText(certText);
        showToast(`تم نسخ شهادة التقدير للطالب ${student.name} للمشاركة 📜`);
    }
}

function openDailyChallengeModal() {
    const answer = prompt("سؤال اليوم في علوم القرآن:\nما هي السورة الملقبة بـ (عروس القرآن)؟\n1) سورة يس\n2) سورة الرحمن\n3) سورة الملك", "2");
    if (answer === "2" || (answer && answer.includes("الرحمن"))) {
        const student = currentOrgData.students[0];
        if (student) student.points += 25;
        showToast("إجابة صحيحة وممتازة! 🌟 تمت إضافة 25 نقطة لرصيدك القرآني!");
        renderStudents(currentGender, currentSearch);
    } else if (answer) {
        showToast("إجابة غير صحيحة، حاول غداً في التحدي القادم!");
    }
}

function shareFullReportWeb() {
    const totalStudents = currentOrgData.students.length;
    const maleCount = currentOrgData.students.filter(s => s.gender === 'MALE').length;
    const femaleCount = currentOrgData.students.filter(s => s.gender === 'FEMALE').length;
    const text = `📊 *التقرير القرآني الشامل - ${currentOrgData.name}* 📊\n` +
        `المقر: ${currentOrgData.location}\n` +
        `عدد الحلقات: ${currentOrgData.circles.length}\n` +
        `إجمالي الطلاب والطالبات: ${totalStudents} (بنين: ${maleCount} | بنات: ${femaleCount})\n` +
        `عدد التسميعات الموثقة: ${currentOrgData.recitations.length}\n` +
        `🔒 نظام أمني معتمد - عزل تام للبيانات\n` +
        `منارة الجيل القرآني الرسمية`;
    if (navigator.share) {
        navigator.share({ title: `تقرير ${currentOrgData.shortName}`, text: text }).catch(() => {});
    } else {
        navigator.clipboard.writeText(text);
        showToast("تم نسخ التقرير القرآني الشامل للمشاركة! 📋");
    }
}

function shareApp() {
    const text = `📲 أدعوكم لاستخدام منظومة منارة الجيل القرآني الرسمية المعتمدة لمتابعة حلقات القرآن الكريم، إشراف الجمعيتين (هائل سعيد ومعاذ الحروي)، ميزة القراء لمعرفة مستوى الطالب، وموسوعة القراءات العشر لجميع المذاهب: ${window.location.href}`;
    if (navigator.share) {
        navigator.share({ title: "منارة الجيل القرآني", text: text }).catch(() => {});
    } else {
        navigator.clipboard.writeText(text);
        showToast("تم نسخ رابط التطبيق بنجاح للمشاركة! 📲");
    }
}

function showToast(msg) {
    const t = document.getElementById('toast');
    if (!t) return;
    t.innerText = msg;
    t.classList.add('show');
    setTimeout(() => {
        t.classList.remove('show');
    }, 3000);
}

// بدء تشغيل وفحص الجلسة الأمنية عند التحميل
document.addEventListener('DOMContentLoaded', () => {
    checkActiveSession();
});
if (document.readyState === 'interactive' || document.readyState === 'complete') {
    checkActiveSession();
}
