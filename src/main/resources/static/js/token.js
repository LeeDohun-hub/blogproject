// URL에서 token 파라미터 있으면 저장
const token = new URLSearchParams(window.location.search).get('token');
if (token) {
    localStorage.setItem("access_token", token);
    // 토큰 저장 후 URL 쿼리 제거 (optional)
    window.history.replaceState({}, document.title, window.location.pathname);
}

// 로그인/로그아웃 버튼 영역 DOM 가져오기
const authArea = document.getElementById('auth-area');

// JWT 토큰 존재 여부 체크
const accessToken = localStorage.getItem('access_token');

if (accessToken) {
    // JWT 페이로드에서 사용자 이름 추출 (sub 필드로 가정)
    const payload = parseJwt(accessToken);
    const username = payload?.sub || 'User';

    authArea.innerHTML = `

      <button id="logout-btn" class="btn-link auth-btn">로그아웃</button>
    `;

    document.getElementById('logout-btn').addEventListener('click', () => {
      localStorage.removeItem('access_token');
      // 필요하면 refresh token도 삭제
      localStorage.removeItem('refresh_token');
      window.location.href = '/';  // 로그아웃 후 홈으로 이동
    });

} else {
    authArea.innerHTML = `<a href="/login" class="nav-link">로그인</a>`;
}

// JWT 페이로드 디코딩 함수
function parseJwt(token) {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64).split('').map(c =>
        '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      ).join('')
    );
    return JSON.parse(jsonPayload);
  } catch (e) {
    return null;
  }
}
