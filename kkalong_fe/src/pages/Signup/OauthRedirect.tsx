import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { parseJwt } from "../../api/local";
import { SET_TOKEN } from "../../redux/modules/Auth";
import {
  setDupEmail,
  setEmail,
  setProvider,
} from "../../redux/modules/registerReducer";
import FadeLoader from "react-spinners/FadeLoader";
import axios from "../../api/axios";
import requests from "../../api/requests";
export const OauthRedirect = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  let token: string = useLocation().search.split("=")[1];
  console.log(token);
  if (token === "google") {
    navigate("/login");
    alert("이미 가입된 유저입니다. 구글로 로그인 해주세요");
  } else if (token === "kakao") {
    navigate("/login");
    alert("이미 가입된 유저입니다. 카카오로 로그인 해주세요");
  } else if (token === "kkalong") {
    navigate("/login");
    alert("이미 가입된 유저입니다. 로그인 해주세요");
  }
  let role = parseJwt(token).roles[0].authority;
  let email = parseJwt(token).sub;
  let provider = parseJwt(token).provider;

  useEffect(() => {
    dispatch(SET_TOKEN(token));
    if (role === "ROLE_USER") {
      localStorage.setItem("token", token);
      axios
        .get(requests.Profile)
        .then((res) => {
          localStorage.setItem("userProfile", JSON.stringify(res.data.user));
        })
        .catch((err) => {
          // localStorage.setItem("userProfile", )
          console.log(err);
        });
      // navigate("/closet");
      console.log(token);
    } else {
      localStorage.setItem("provider", provider);
      localStorage.setItem("token", token);
      navigate("/signup", {
        state: token,
      });
    }

    dispatch(setEmail(email));
    dispatch(setDupEmail(true));
  });

  return (
    <div className="contentWrap">
      <div
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
        }}
      >
        <FadeLoader
          color="#C63DEE"
          height={15}
          width={5}
          radius={2}
          margin={2}
        />
      </div>
    </div>
  );
};
