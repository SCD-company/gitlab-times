/* tslint:disable */
/* eslint-disable */

export interface HttpClient<O> {
  request<R>(requestConfig: {
    method: string;
    url: string;
    queryParams?: any;
    data?: any;
    copyFn?: (data: R) => R;
    options?: O;
  }): RestResponse<R>;
}

export class RestApplicationClient<O> {
  constructor(protected httpClient: HttpClient<O>) {}

  /**
   * HTTP POST /api/options
   * Java method: com.scd.gitlabtimeback.controller.OptionsController.getOptions
   */
  getOptions(reportParametersRequestDto: ReportParametersRequestDto, options?: O): RestResponse<OptionsDto> {
    return this.httpClient.request({
      method: 'POST',
      url: uriEncoding`api/options`,
      data: reportParametersRequestDto,
      options: options,
    });
  }

  /**
   * HTTP POST /api/report_download
   * Java method: com.scd.gitlabtimeback.controller.TimeLogController.getCsvReport
   */
  getCsvReport(reportParametersRequestDto: ReportParametersRequestDto, options?: O): RestResponse<DataFileDto> {
    return this.httpClient.request({
      method: 'POST',
      url: uriEncoding`api/report_download`,
      data: reportParametersRequestDto,
      options: options,
    });
  }

  /**
   * HTTP POST /api/report_download_pdf
   * Java method: com.scd.gitlabtimeback.controller.TimeLogController.getPdfReport
   */
  getPdfReport(reportParametersRequestDto: ReportParametersRequestDto, options?: O): RestResponse<DataFileDto> {
    return this.httpClient.request({
      method: 'POST',
      url: uriEncoding`api/report_download_pdf`,
      data: reportParametersRequestDto,
      options: options,
    });
  }

  /**
   * HTTP POST /api/report_get
   * Java method: com.scd.gitlabtimeback.controller.TimeLogController.getReportWithGrouping
   */
  getReportWithGrouping(
    reportParametersRequestDto: ReportParametersRequestDto,
    options?: O
  ): RestResponse<GroupingReportDto[]> {
    return this.httpClient.request({
      method: 'POST',
      url: uriEncoding`api/report_get`,
      data: reportParametersRequestDto,
      options: options,
    });
  }

  /**
   * HTTP GET /api/user
   * Java method: com.scd.gitlabtimeback.controller.UserController.getUser
   */
  getUser(options?: O): RestResponse<UserDetailsDto> {
    return this.httpClient.request({ method: 'GET', url: uriEncoding`api/user`, options: options });
  }

  /**
   * HTTP GET /auth/login
   * Java method: com.scd.gitlabtimeback.controller.authController.login
   */
  login(options?: O): RestResponse<void> {
    return this.httpClient.request({ method: 'GET', url: uriEncoding`auth/login`, options: options });
  }

  /**
   * HTTP GET /auth/logout
   * Java method: com.scd.gitlabtimeback.controller.authController.logout
   */
  logout(options?: O): RestResponse<void> {
    return this.httpClient.request({ method: 'GET', url: uriEncoding`auth/logout`, options: options });
  }
}

export interface DataFileDto {
  content: any;
  fileName: string;
}

export interface DateBoundaryDto {
  day: number;
  month: number;
  year: number;
}

export interface GroupingDto {
  grouping: GroupingByField;
  value: string;
}

export interface GroupingReportDto {
  actual: boolean;
  id: number;
  name: string;
  subGroup: GroupingReportDto[];
  time: number;
  type: GroupingByField;
}

export interface OptionSetDto {
  project: ProjectDto;
  user: UserDto;
}

export interface OptionsDto {
  projects: ProjectDto[];
  users: UserDto[];
}

export interface PeriodDto {
  period?: Period;
  value: string;
}

export interface ProjectDto {
  archived: boolean;
  id: number;
  name: string;
}

export interface ReportItemDto {
  issueName: string;
  projectName: string;
  spendTime: number;
  userName: string;
}

export interface ReportParametersRequestDto {
  dateFrom?: DateBoundaryDto;
  dateTo?: DateBoundaryDto;
  grouping?: GroupingByField[];
  issueId?: number;
  period?: Period;
  projectId?: number;
  useSpentTime?: boolean;
  userId?: number;
}

export interface SearchData {
  from: number;
  issueId: number;
  period: Period;
  projectId: number;
  to: number;
  useSpentTime: boolean;
  userId: number;
}

export interface TimeLogDto {
  createdAt: number;
  id: number;
  issue: string;
  project: ProjectDto;
  timeSpent: number;
  user: UserDto;
}

export interface UserDetailsDto {
  admin: boolean;
  authenticated: boolean;
  id: number;
  name: string;
}

export interface UserDto {
  id: number;
  name: string;
  state: string;
}

export type RestResponse<R> = Promise<Axios.GenericAxiosResponse<R>>;

export enum GroupingByField {
  ISSUE = 'ISSUE',
  PROJECT = 'PROJECT',
  PERSON = 'PERSON',
  MONTH = 'MONTH',
  MONTH_SPENT = 'MONTH_SPENT',
}

export enum Period {
  CUR_MONTH = 'CUR_MONTH',
  LAST_MONTH = 'LAST_MONTH',
  CUR_WEEK = 'CUR_WEEK',
  LAST_WEEK = 'LAST_WEEK',
  CUSTOM = 'CUSTOM',
}

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
  let result = '';
  for (let i = 0; i < substitutions.length; i++) {
    result += template[i];
    result += encodeURIComponent(substitutions[i]);
  }
  result += template[template.length - 1];
  return result;
}

// Added by 'AxiosClientExtension' extension

import axios from 'axios';
import * as Axios from 'axios';

declare module 'axios' {
  export interface GenericAxiosResponse<R> extends Axios.AxiosResponse {
    data: R;
  }
}

class AxiosHttpClient implements HttpClient<Axios.AxiosRequestConfig> {
  constructor(private axios: Axios.AxiosInstance) {}

  request<R>(requestConfig: {
    method: string;
    url: string;
    queryParams?: any;
    data?: any;
    copyFn?: (data: R) => R;
    options?: Axios.AxiosRequestConfig;
  }): RestResponse<R> {
    function assign(target: any, source?: any) {
      if (source != undefined) {
        for (const key in source) {
          if (source.hasOwnProperty(key)) {
            target[key] = source[key];
          }
        }
      }
      return target;
    }

    const config: Axios.AxiosRequestConfig = {};
    config.method = requestConfig.method as typeof config.method; // `string` in axios 0.16.0, `Method` in axios 0.19.0
    config.url = requestConfig.url;
    config.params = requestConfig.queryParams;
    config.data = requestConfig.data;
    assign(config, requestConfig.options);
    const copyFn = requestConfig.copyFn;

    const axiosResponse = this.axios.request(config);
    return axiosResponse.then((axiosResponse) => {
      if (copyFn && axiosResponse.data) {
        (axiosResponse as any).originalData = axiosResponse.data;
        axiosResponse.data = copyFn(axiosResponse.data);
      }
      return axiosResponse;
    });
  }
}

export class AxiosRestApplicationClient extends RestApplicationClient<Axios.AxiosRequestConfig> {
  constructor(baseURL: string, axiosInstance: Axios.AxiosInstance = axios.create()) {
    axiosInstance.defaults.baseURL = baseURL;
    super(new AxiosHttpClient(axiosInstance));
  }
}
